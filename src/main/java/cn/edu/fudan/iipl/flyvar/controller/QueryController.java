/**
 * ychen. Copyright (c) 2016年10月26日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.FlyvarSystemException;
import cn.edu.fudan.iipl.flyvar.form.QueryForm;
import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.VariationRegion;
import cn.edu.fudan.iipl.flyvar.model.constants.QueryType;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;
import cn.edu.fudan.iipl.flyvar.service.QueryService;
import cn.edu.fudan.iipl.flyvar.service.SampleNameService;

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

    private static final String     QUERY_JSP                  = "query/query";

    private static final String     QUERY_RESULT_JSP           = "query/queryResult";

    private static final String     QUERY_BY_SAMPLE_RESULT_JSP = "query/sendEmailSuccess";

    @Autowired
    private PathUtils               pathUtils;

    @Autowired
    private SampleNameService       sampleNameService;

    @Autowired
    private QueryService            queryService;

    @Autowired
    private FlyvarMailSenderService flyvarMailSenderService;

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
                success = queryByVariation(queryForm, bindings, queryFile, redirectModel, model);
                return success ? "redirect:/query/result.htm" : QUERY_JSP;
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
                                     BindingResult bindings, MultipartFile queryFile, Model model) {
        checkReferer(request);
        boolean collectedParams = validateQueryParams(request, queryForm, bindings, queryFile,
            model);
        if (!collectedParams) {
            return QUERY_JSP;
        }
        Set<Variation> variations = null;
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(queryForm.getQueryInput())) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        variations = Variation.convertInputToVariations(variationStr);
        if (variations == null) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            return QUERY_JSP;
        }
        return null;
    }

    @RequestMapping(value = { "/query/result.htm" }, method = { RequestMethod.GET,
                                                                RequestMethod.POST })
    public String showQueryResults(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("queryResults")) {
            throw new RuntimeException("Invalid access!");
        }
        return QUERY_RESULT_JSP;
    }

    @RequestMapping(value = { "/query/sample/success.htm" }, method = { RequestMethod.GET,
                                                                        RequestMethod.POST })
    public String bySampleResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("success")) {
            throw new RuntimeException("Invalid access!");
        }
        return QUERY_BY_SAMPLE_RESULT_JSP;
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
            if (!queryForm.getQueryEmail().matches("^(.+)@(.+)$")) {
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
     * query by variation processing.
     * @param queryForm
     * @param bindings
     * @param queryFile
     * @param redirectModel
     * @param model
     * @return true if processing successfully, false else.
     */
    private boolean queryByVariation(@Valid QueryForm queryForm, BindingResult bindings,
                                     MultipartFile queryFile, RedirectAttributes redirectModel,
                                     Model model) {
        Set<Variation> variations = null;
        String variationStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(queryForm.getQueryInput())) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        variations = Variation.convertInputToVariations(variationStr);
        if (variations == null) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return false;
        }
        List<QueryResultVariation> queryResult = queryService.queryByVariation(variations,
            VariationDataBaseType.of(queryForm.getVariationDb()));
        redirectModel.addFlashAttribute("queryResults", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query by region processing.
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
        Set<VariationRegion> regions = null;
        String regionStr = queryForm.getQueryInput();
        if (StringUtils.isBlank(queryForm.getQueryInput())) {
            regionStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(queryFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }
        regions = VariationRegion.convertInputToRegions(regionStr);
        if (regions == null) {
            model.addAttribute("queryForm", queryForm);
            bindings.rejectValue("queryInput", "error.queryInputFormat");
            logger.info("error submit! error format for variation input or file: queryForm={}",
                queryForm);
            return false;
        }
        List<QueryResultVariation> queryResult = queryService.queryByRegion(regions,
            VariationDataBaseType.of(queryForm.getVariationDb()));
        redirectModel.addFlashAttribute("queryResults", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query by gene name whole region processing.
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
        if (StringUtils.isBlank(queryForm.getQueryInput())) {
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
        redirectModel.addFlashAttribute("queryResults", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }

    /**
     * query by gene name exon region processing.
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
        if (StringUtils.isBlank(queryForm.getQueryInput())) {
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
        redirectModel.addFlashAttribute("queryResults", queryResult);
        redirectModel.addFlashAttribute("queryType", queryForm.getVariationDb());
        return true;
    }
}
