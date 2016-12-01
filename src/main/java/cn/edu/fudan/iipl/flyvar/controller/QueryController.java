/**
 * ychen. Copyright (c) 2016年10月26日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.CombineAnnotateResultException;
import cn.edu.fudan.iipl.flyvar.exception.FlyvarSystemException;
import cn.edu.fudan.iipl.flyvar.exception.InvalidAccessException;
import cn.edu.fudan.iipl.flyvar.exception.NotFoundException;
import cn.edu.fudan.iipl.flyvar.form.QueryForm;
import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.VariationRegion;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.model.constants.QueryType;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;
import cn.edu.fudan.iipl.flyvar.service.QueryService;
import cn.edu.fudan.iipl.flyvar.service.SampleNameService;
import cn.edu.fudan.iipl.flyvar.vo.SampleLinkVO;

/**
 * variantion query控制器
 * 
 * @author racing
 * @version $Id: QueryController.java, v 0.1 2016年10月26日 下午10:02:56 racing Exp $
 */
@Controller
public class QueryController extends AbstractController {

    private static final Logger     logger                     = LoggerFactory
        .getLogger(QueryController.class);

    private static final int        ASYNC_SIZE                 = 300000;

    private static final String     QUERY_JSP                  = "query/query";

    private static final String     QUERY_RESULT_JSP           = "query/result";

    private static final String     QUERY_BY_SAMPLE_RESULT_JSP = "query/sendEmailSuccess";

    private static final String     SAMPLE_LIST_JSP            = "query/sampleList";

    private static final String     QUERY_ASYNC_RESULT_JSP     = "query/asyncResult";

    private static final String     EMAIL_PATTERN              = "^(.+)@(.+)$";

    @Autowired
    private PathUtils               pathUtils;

    @Autowired
    private SampleNameService       sampleNameService;

    @Autowired
    private QueryService            queryService;

    @Autowired
    private AnnotateService         annotateService;

    @Autowired
    private FlyvarMailSenderService flyvarMailSenderService;

    @Autowired
    private AnnovarUtils            annovarUtils;

    @Autowired
    private CacheService            cacheService;

    @RequestMapping(value = { "/query.htm" }, method = { RequestMethod.GET })
    public String showQuery(Model model) {
        model.addAttribute("queryForm", new QueryForm());
        return QUERY_JSP;
    }

    @RequestMapping(value = { "/query/getDgrpSampleList.json" }, method = { RequestMethod.GET,
                                                                            RequestMethod.POST })
    @ResponseBody
    public Map<String, List<String>> getSampleNames(HttpServletRequest request) {
        checkReferer(request);
        Map<String, List<String>> result = Maps.newHashMap();
        result.put("data", sampleNameService.getSampleNames());
        return result;
    }

    @RequestMapping(value = { "/query/query.htm" }, method = { RequestMethod.POST })
    public String doQuery(HttpServletRequest request, @Valid QueryForm queryForm,
                          BindingResult bindings, MultipartFile queryFile,
                          RedirectAttributes redirectModel, Model model) {
        checkReferer(request);
        boolean correctParams = validateQueryParams(request, queryForm, bindings, queryFile, model);
        if (!correctParams) {
            return QUERY_JSP;
        }
        QueryType queryType = QueryType.of(queryForm.getQueryType());

        boolean success = false;
        switch (queryType) {
            case VARIATION:
                String variationStr = queryForm.getQueryInput();
                if (StringUtils.isBlank(variationStr)) {
                    variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                        FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                            pathUtils.getAbsoluteUploadFilesPath().toString()));
                }
                Set<Variation> variations = Variation.convertInputToVariations(variationStr);
                if (CollectionUtils.isEmpty(variations)) {
                    model.addAttribute("queryForm", queryForm);
                    bindings.rejectValue("queryInput", "error.queryInputFormat");
                    logger.info(
                        "error submit! error format for variation input or file: queryForm={}",
                        queryForm);
                    return QUERY_JSP;
                }

                /** if lines of the variations to query is above ASYNC_SIZE, do async annotate */
                if (StringUtils.isNotBlank(queryForm.getQueryEmail())
                    && queryForm.getQueryEmail().matches(EMAIL_PATTERN)
                    || variations.size() > ASYNC_SIZE) {
                    if (StringUtils.isBlank(queryForm.getQueryEmail())
                        || !queryForm.getQueryEmail().matches(EMAIL_PATTERN)) {
                        model.addAttribute("queryForm", queryForm);
                        bindings.rejectValue("queryForm", "error.queryForm");
                        logger.info("error submit! error format for queryEmail: queryForm={}",
                            queryForm);
                        return QUERY_JSP;
                    }
                    queryService.asyncQueryAndSendEmail(variations,
                        VariationDataBaseType.of(queryForm.getVariationDb()),
                        queryForm.getQueryEmail());
                    redirectModel.addFlashAttribute("asyncSuccess", true);
                    return "redirect:/query/async/result.htm";
                }

                List<QueryResultVariation> queryResult = queryService.queryByVariation(variations,
                    VariationDataBaseType.of(queryForm.getVariationDb()));
                redirectModel.addFlashAttribute("queryResult", queryResult);
                redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
                return "redirect:/query/result.htm";
            case SAMPLE:
                String realSampleName = "DGRP-" + queryForm.getSelectSample().substring(5, 8)
                                        + ".SNP.only_homo";
                flyvarMailSenderService.sendSnpSample(Lists.newArrayList(realSampleName),
                    queryForm.getQueryEmail());
                redirectModel.addFlashAttribute("success", "success");
                return "redirect:/query/sample/success.htm";
            case REGION:
                success = queryByRegion(queryForm, bindings, queryFile, redirectModel, model);
                return success ? "redirect:/query/result.htm" : QUERY_JSP;
            case GENE_WHOLE:
                success = queryByGeneNameWhole(queryForm, bindings, queryFile, redirectModel,
                    model);
                return success ? "redirect:/query/result.htm" : QUERY_JSP;
            case GENE_EXON:
                success = queryByGeneNameExon(queryForm, bindings, queryFile, redirectModel, model);
                return success ? "redirect:/query/result.htm" : QUERY_JSP;
            default:
                throw new FlyvarSystemException("Error submit!");
        }
    }

    @RequestMapping(value = { "/query/annotate.htm" }, method = { RequestMethod.POST })
    public String doQueryAndAnnotate(HttpServletRequest request, @Valid QueryForm queryForm,
                                     BindingResult bindings, MultipartFile queryFile,
                                     RedirectAttributes redirectModel, Model model) {
        checkReferer(request);
        if (!validateQueryParams(request, queryForm, bindings, queryFile, model)) {
            return QUERY_JSP;
        }
        QueryType queryType = QueryType.of(queryForm.getQueryType());

        List<QueryResultVariation> queryResult = null;
        switch (queryType) {
            case VARIATION:
                queryResult = queryAnnotateByVariation(queryForm, bindings, queryFile,
                    redirectModel, model);
                break;
            case SAMPLE:
                boolean success = queryAnnotateBySample(queryForm, bindings, queryFile,
                    redirectModel, model);
                return success ? "redirect:/annotate/result.htm" : QUERY_JSP;
            case REGION:
                queryResult = queryAnnotateByRegion(queryForm, bindings, queryFile, redirectModel,
                    model);
                break;
            case GENE_WHOLE:
                queryResult = queryAnnotateByGeneNameWhole(queryForm, bindings, queryFile,
                    redirectModel, model);
                break;
            case GENE_EXON:
                queryResult = queryAnnotateByGeneNameExon(queryForm, bindings, queryFile,
                    redirectModel, model);
                break;
            default:
                throw new FlyvarSystemException("Error submit!");
        }
        if (queryResult == null) {
            return QUERY_JSP;
        }

        /** if lines of the variations to annotate is above ASYNC_SIZE, do async annotate */
        if (StringUtils.isNotBlank(queryForm.getQueryEmail())
            && queryForm.getQueryEmail().matches(EMAIL_PATTERN)
            || QueryType.VARIATION == QueryType.of(queryForm.getQueryType())
               && queryResult.size() > ASYNC_SIZE) {
            if (StringUtils.isBlank(queryForm.getQueryEmail())
                || !queryForm.getQueryEmail().matches(EMAIL_PATTERN)) {
                model.addAttribute("queryForm", queryForm);
                bindings.rejectValue("queryForm", "error.queryForm");
                logger.info("error submit! error format for queryEmail: queryForm={}", queryForm);
                return QUERY_JSP;
            }
            queryService.asyncAnnotateAndSendEmail(queryResult, queryForm.getQueryEmail());
            redirectModel.addFlashAttribute("asyncSuccess", true);
            return "redirect:/annotate/async/result.htm";
        }

        Path annotateResultVcfPath = queryService.annotateResultVariation(queryResult);
        addAnnotateRedirectAttributes(redirectModel, annotateResultVcfPath);
        return "redirect:/annotate/result.htm";
    }

    @RequestMapping(value = { "/query/result.htm" }, method = { RequestMethod.GET })
    public String showQueryResults(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("queryResult")) {
            throw new InvalidAccessException("Invalid access!");
        }
        return QUERY_RESULT_JSP;
    }

    @RequestMapping(value = { "/query/async/result.htm" }, method = { RequestMethod.GET })
    public String showAsyncQueryResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("asyncSuccess")) {
            throw new InvalidAccessException("Invalid access!");
        }
        return QUERY_ASYNC_RESULT_JSP;
    }

    @RequestMapping(value = { "/query/result/{filename:.+}" }, method = { RequestMethod.GET })
    public ResponseEntity<byte[]> downloadQueryResult(HttpServletRequest request,
                                                      @PathVariable String filename,
                                                      Model model) throws IOException {
        if (StringUtils.isBlank(filename)) {
            logger.warn("filename is blank!");
            throw new NotFoundException();
        }
        Path filePath = pathUtils.getAbsoluteAnnotationFilesPath().resolve(filename);
        if (!filePath.toFile().exists()) {
            logger.warn("file not exists! filename={}", filename);
            throw new NotFoundException();
        }

        String mimeType = URLConnection.guessContentTypeFromName(filePath.getFileName().toString());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
            // logger.info("mimetype is not detectable, will take default. mimeType={}", mimeType);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", filePath.getFileName().toString());
        logger.info("download file! ip={}, filePath={}", getClientIP(request), filePath);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath.toFile()), headers,
            HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/query/sample/success.htm" }, method = { RequestMethod.GET })
    public String bySampleResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("success")) {
            throw new InvalidAccessException("Invalid access!");
        }
        return QUERY_BY_SAMPLE_RESULT_JSP;
    }

    @RequestMapping(value = { "/query/sample/list.htm" }, method = { RequestMethod.GET })
    private String variationSampleList(HttpServletRequest request, @RequestParam String chr,
                                       @RequestParam Long pos, @RequestParam String ref,
                                       @RequestParam String alt, Model model) {
        if (!ref.matches("[ATCG]+") || !alt.matches("[ATCG]+")) {
            throw new NotFoundException();
        }
        Variation variation = new Variation(chr, pos, ref, alt);
        List<Pair<String, String>> linkPairs = sampleNameService
            .getSampleLinkPair(sampleNameService.getSampleNames(variation));
        List<SampleLinkVO> sampleLinks = linkPairs.stream()
            .map(pair -> SampleLinkVO.convertToSampleLinkVO(pair)).collect(Collectors.toList());
        model.addAttribute("sampleListResult", sampleLinks);
        return SAMPLE_LIST_JSP;
    }

    private boolean validateQueryParams(HttpServletRequest request, @Valid QueryForm queryForm,
                                        BindingResult bindings, MultipartFile queryFile,
                                        Model model) {
        if (bindings.hasErrors()) {
            model.addAttribute("queryForm", queryForm);
            logger.info("error submit! QueryForm is empty: queryForm={}", queryForm);
            return false;
        }
        if (QueryType.SAMPLE == QueryType.of(queryForm.getQueryType())) {
            if (StringUtils.isBlank(queryForm.getSelectSample())
                || !queryForm.getSelectSample().matches("dgrp_\\d{3}_snp_only_homo")) {
                model.addAttribute("queryForm", queryForm);
                bindings.rejectValue("selectSample", "error.selectSample");
                logger.info("error submit! selectSample is error or empty: queryForm={}",
                    queryForm);
                return false;
            }
            if (!queryForm.getQueryEmail().matches(EMAIL_PATTERN)) {
                model.addAttribute("queryForm", queryForm);
                bindings.rejectValue("queryEmail", "error.queryEmail");
                logger.info("error submit! queryEmail is empty: queryForm={}", queryForm);
                return false;
            }
            return true;
        }
        if (StringUtils.isBlank(queryForm.getQueryInput()) && queryFile.isEmpty()) {
            logger.info(
                "error submit! Both queryForm and queryFile are empty!" + getClientIP(request));
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInput");
            return false;
        }
        return true;
    }

    /**
     * add annotate redirect attributes
     * 
     * @param redirectModel
     * @param queryResult
     */
    private void addAnnotateRedirectAttributes(RedirectAttributes redirectModel,
                                               Path annotateResultVcfPath) {

        Path annovarInputPath = annovarUtils
            .getAnnovarInputPath(annotateResultVcfPath.getFileName().toString());
        Path annotateResultPath = annovarUtils
            .getAnnotatePath(annotateResultVcfPath.getFileName().toString());
        Path exonicAnnotatePath = annovarUtils
            .getExonicAnnotatePath(annotateResultVcfPath.getFileName().toString());
        Path combineAnnovarOutPath = annovarUtils
            .getCombineAnnovarOutPath(annotateResultVcfPath.getFileName().toString());
        Path annovarInvalidInputPath = annovarUtils
            .getAnnovarInvalidInputPath(annotateResultVcfPath.getFileName().toString());

        redirectModel.addFlashAttribute("annovarInput", annovarInputPath.getFileName().toString());
        redirectModel.addFlashAttribute("annotateResult",
            annotateResultPath.getFileName().toString());
        redirectModel.addFlashAttribute("exonicAnnotateResult",
            exonicAnnotatePath.getFileName().toString());
        if (exonicAnnotatePath.toFile().length() > 0) {
            Path combinedAnnotateResultPath = null;
            try {
                combinedAnnotateResultPath = annotateService
                    .mergeAnnotateResult(annovarInputPath.getFileName().toString());
                redirectModel.addFlashAttribute("combinedExonicResult",
                    combinedAnnotateResultPath.getFileName().toString());
            } catch (CombineAnnotateResultException ex) {
            }
        }
        redirectModel.addFlashAttribute("combineAnnovarOut",
            combineAnnovarOutPath.getFileName().toString());
        if (annovarInvalidInputPath.toFile().exists()) {
            redirectModel.addFlashAttribute("annovarInvalidInput",
                annovarInvalidInputPath.getFileName().toString());
        }
    }

    /**
     * query and annotate by variation processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return list if query success, null else.
     */
    private List<QueryResultVariation> queryAnnotateByVariation(@Valid QueryForm queryForm,
                                                                BindingResult bindings,
                                                                MultipartFile queryFile,
                                                                RedirectAttributes redirectModel,
                                                                Model model) {
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        Set<Variation> variations = Variation.convertInputToVariations(variationStr);
        if (CollectionUtils.isEmpty(variations)) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return null;
        }
        return queryService.queryByVariation(variations,
            VariationDataBaseType.of(queryForm.getVariationDb()));
    }

    /**
     * query and annotate by sample processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return true if processing successfully, false else.
     */
    private boolean queryAnnotateBySample(@Valid QueryForm queryForm, BindingResult bindings,
                                          MultipartFile queryFile, RedirectAttributes redirectModel,
                                          Model model) {
        String realSampleName = "DGRP-" + queryForm.getSelectSample().substring(5, 8)
                                + ".SNP.only_homo";
        String sampleVariationsKey = Constants.CACHE_SAMPLE_VARIATIONS + realSampleName;
        Set<Variation> variations = cacheService.get(sampleVariationsKey);
        if (variations == null) {
            String variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                pathUtils.getAbsoluteSnpSamplesPath().resolve(realSampleName));
            variations = Variation.convertInputToVariations(variationStr);
            if (CollectionUtils.isEmpty(variations)) {
                model.addAttribute("queryForm", queryForm);
                bindings.rejectValue("selectSample", "error.selectSample");
                logger.info("error submit! error format for selectSample: queryForm={}", queryForm);
                return false;
            }
            cacheService.set(sampleVariationsKey, variations);
        }
        String sampleAnnotateResultKey = Constants.CACHE_SAMPLE_ANNOTATE_RESULT + realSampleName;
        String cachePath = cacheService.get(sampleAnnotateResultKey);
        Path annotateResultVcfPath = null;
        if (cachePath == null) {
            Path vcfFormatVariationPath = annotateService.convertVariationsToVcfFile(variations);
            annotateResultVcfPath = annotateService
                .annotateVcfFormatVariation(vcfFormatVariationPath);
            cacheService.set(sampleAnnotateResultKey, annotateResultVcfPath.toString());
        } else {
            annotateResultVcfPath = Paths.get(cachePath);
        }
        addAnnotateRedirectAttributes(redirectModel, annotateResultVcfPath);
        return true;
    }

    /**
     * query by region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return true if processing successfully, false else.
     */
    private boolean queryByRegion(@Valid QueryForm queryForm, BindingResult bindings,
                                  MultipartFile queryFile, RedirectAttributes redirectModel,
                                  Model model) {
        String regionStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(regionStr)) {
            regionStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        Set<VariationRegion> regions = VariationRegion.convertInputToRegions(regionStr);
        if (regions == null) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return false;
        }
        List<QueryResultVariation> queryResult = queryService.queryByRegion(regions,
            VariationDataBaseType.of(queryForm.getVariationDb()));

        redirectModel.addFlashAttribute("queryResult", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query and annotate by region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return  list if query success, null else.
     */
    private List<QueryResultVariation> queryAnnotateByRegion(@Valid QueryForm queryForm,
                                                             BindingResult bindings,
                                                             MultipartFile queryFile,
                                                             RedirectAttributes redirectModel,
                                                             Model model) {
        String regionStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(regionStr)) {
            regionStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        Set<VariationRegion> regions = VariationRegion.convertInputToRegions(regionStr);
        if (regions == null) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return null;
        }
        return queryService.queryByRegion(regions,
            VariationDataBaseType.of(queryForm.getVariationDb()));
    }

    /**
     * query by gene name whole region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return true if processing successfully, false else.
     */
    private boolean queryByGeneNameWhole(@Valid QueryForm queryForm, BindingResult bindings,
                                         MultipartFile queryFile, RedirectAttributes redirectModel,
                                         Model model) {
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        if (StringUtils.isBlank(variationStr)) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return false;
        }
        List<String> geneNames = Lists.newArrayList(variationStr.split("\\s+"));
        List<QueryResultVariation> queryResult = queryService.queryByGeneNameWholeRegion(geneNames,
            VariationDataBaseType.of(queryForm.getVariationDb()));
        redirectModel.addFlashAttribute("queryResult", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query and annotate by gene name whole region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return list if query success, null else.
     */
    private List<QueryResultVariation> queryAnnotateByGeneNameWhole(@Valid QueryForm queryForm,
                                                                    BindingResult bindings,
                                                                    MultipartFile queryFile,
                                                                    RedirectAttributes redirectModel,
                                                                    Model model) {
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        if (StringUtils.isBlank(variationStr)) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return null;
        }
        List<String> geneNames = Lists.newArrayList(variationStr.split("\\s+"));
        return queryService.queryByGeneNameWholeRegion(geneNames,
            VariationDataBaseType.of(queryForm.getVariationDb()));
    }

    /**
     * query by gene name exon region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return true if processing successfully, false else.
     */
    private boolean queryByGeneNameExon(@Valid QueryForm queryForm, BindingResult bindings,
                                        MultipartFile queryFile, RedirectAttributes redirectModel,
                                        Model model) {
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        if (StringUtils.isBlank(variationStr)) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return false;
        }
        List<String> geneNames = Lists.newArrayList(variationStr.split("\\s+"));
        List<QueryResultVariation> queryResult = queryService.queryByGeneNameExonRegion(geneNames,
            VariationDataBaseType.of(queryForm.getVariationDb()));
        redirectModel.addFlashAttribute("queryResult", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query and annotate by gene name exon region processing.
     * 
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return  list if query success, null else.
     */
    private List<QueryResultVariation> queryAnnotateByGeneNameExon(@Valid QueryForm queryForm,
                                                                   BindingResult bindings,
                                                                   MultipartFile queryFile,
                                                                   RedirectAttributes redirectModel,
                                                                   Model model) {
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        if (StringUtils.isBlank(variationStr)) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return null;
        }
        List<String> geneNames = Lists.newArrayList(variationStr.split("\\s+"));
        return queryService.queryByGeneNameExonRegion(geneNames,
            VariationDataBaseType.of(queryForm.getVariationDb()));
    }
}
