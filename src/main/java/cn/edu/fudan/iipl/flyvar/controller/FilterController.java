/**
 * ychen. Copyright (c) 2016年11月13日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.CombineAnnotateResultException;
import cn.edu.fudan.iipl.flyvar.exception.InvalidAccessException;
import cn.edu.fudan.iipl.flyvar.exception.NotFoundException;
import cn.edu.fudan.iipl.flyvar.form.FilterForm;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.FilterInputType;
import cn.edu.fudan.iipl.flyvar.model.constants.RemoveDispensableType;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.FilterService;

/**
 * variation filter controller
 * 
 * @author racing
 * @version $Id: FilterController.java, v 0.1 2016年11月13日 下午10:31:07 racing Exp $
 */
@Controller
public class FilterController extends AbstractController {

    private static final Logger logger                  = LoggerFactory
        .getLogger(FilterController.class);

    private static final int    ASYNC_SIZE              = 300000;

    private static final String FILTER_JSP              = "filter/filter";

    private static final String FILTER_RESULT_JSP       = "filter/result";

    private static final String FILTER_ASYNC_RESULT_JSP = "filter/asyncResult";

    private static final String EMAIL_PATTERN           = "^(.+)@(.+)$";

    @Autowired
    private PathUtils           pathUtils;

    @Autowired
    private AnnotateService     annotateService;

    @Autowired
    private AnnovarUtils        annovarUtils;

    @Autowired
    private FilterService       filterService;

    @RequestMapping(value = { "/filter.htm" }, method = { RequestMethod.GET })
    public String showAnnotate(Model model) {
        model.addAttribute("filterForm", new FilterForm());
        return FILTER_JSP;
    }

    @RequestMapping(value = { "/filter/filter.htm" }, method = { RequestMethod.POST })
    public String doFilter(HttpServletRequest request, @Valid FilterForm filterForm,
                           BindingResult bindings, MultipartFile filterFile,
                           RedirectAttributes redirectModel, Model model) {
        checkReferer(request);
        boolean correctParams = validateFilterParams(request, filterForm, bindings, filterFile,
            model);
        if (!correctParams) {
            return FILTER_JSP;
        }

        String variationStr = filterForm.getFilterInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(filterFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }

        Set<Variation> variations = null;
        if (FilterInputType
            .of(filterForm.getInputFormatType()) == FilterInputType.FOUR_COLUMN_TAB_SEPARATED) {
            variations = Variation.convertInputToVariations(variationStr);
        } else if (FilterInputType
            .of(filterForm.getInputFormatType()) == FilterInputType.VCF_FORMAT) {
            variations = Variation.convertVcfInputToVariations(variationStr);
        }
        if (CollectionUtils.isEmpty(variations)) {
            model.addAttribute("filterForm", filterForm);
            bindings.rejectValue("filterInput", "error.filter.inputFormatType");
            logger.info("error submit! error format for variation input or file: filterForm={}",
                filterForm);
            return FILTER_JSP;
        }

        /** if the file size is above ASYNC_SIZE, do async filter */
        if (StringUtils.isNotBlank(filterForm.getFilterEmail())
            && filterForm.getFilterEmail().matches(EMAIL_PATTERN)
            || variations.size() > ASYNC_SIZE) {
            if (StringUtils.isBlank(filterForm.getFilterEmail())
                || !filterForm.getFilterEmail().matches(EMAIL_PATTERN)) {
                model.addAttribute("filterForm", filterForm);
                bindings.rejectValue("filterEmail", "error.filter.filterEmail");
                logger.info("error submit! error format for filterEmail: filterForm={}",
                    filterForm);
                return FILTER_JSP;
            }
            /** async filter variations. This operation will process data background and send results via email to user later. */
            filterService.asyncFilterAndSendEmail(variations,
                VariationDataBaseType.of(filterForm.getVariationDb()),
                RemoveDispensableType.of(filterForm.getRemoveDispensable()),
                filterForm.getFilterEmail());
            redirectModel.addFlashAttribute("asyncSuccess", true);
            return "redirect:/filter/async/result.htm";
        }

        List<Variation> filterResult = filterService.filterVariations(variations,
            VariationDataBaseType.of(filterForm.getVariationDb()));
        if (RemoveDispensableType
            .of(filterForm.getRemoveDispensable()) == RemoveDispensableType.YES) {
            filterResult = filterService.filterDispensableGeneVariations(variations);
        }
        redirectModel.addFlashAttribute("filterResult", filterResult);
        return "redirect:/filter/result.htm";
    }

    @RequestMapping(value = { "/filter/annotate.htm" }, method = { RequestMethod.POST })
    public String doFilterAndAnnotate(HttpServletRequest request, @Valid FilterForm filterForm,
                                      BindingResult bindings, MultipartFile filterFile,
                                      RedirectAttributes redirectModel, Model model) {
        checkReferer(request);
        boolean correctParams = validateFilterParams(request, filterForm, bindings, filterFile,
            model);
        if (!correctParams) {
            return FILTER_JSP;
        }

        String variationStr = filterForm.getFilterInput();
        if (StringUtils.isBlank(variationStr)) {
            variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                FlyvarFileUtils.saveUploadFileAndGetFilePath(filterFile,
                    pathUtils.getAbsoluteUploadFilesPath().toString()));
        }

        Set<Variation> variations = null;
        if (FilterInputType
            .of(filterForm.getInputFormatType()) == FilterInputType.FOUR_COLUMN_TAB_SEPARATED) {
            variations = Variation.convertInputToVariations(variationStr);
        } else if (FilterInputType
            .of(filterForm.getInputFormatType()) == FilterInputType.VCF_FORMAT) {
            variations = Variation.convertVcfInputToVariations(variationStr);
        }
        if (CollectionUtils.isEmpty(variations)) {
            model.addAttribute("filterForm", filterForm);
            bindings.rejectValue("filterInput", "error.filter.inputFormatType");
            logger.info("error submit! error format for variation input or file: filterForm={}",
                filterForm);
            return FILTER_JSP;
        }

        /** if the variation size is above ASYNC_SIZE, do async filter */
        if (StringUtils.isNotBlank(filterForm.getFilterEmail())
            && filterForm.getFilterEmail().matches(EMAIL_PATTERN)
            || variations.size() > ASYNC_SIZE) {
            if (StringUtils.isBlank(filterForm.getFilterEmail())
                || !filterForm.getFilterEmail().matches(EMAIL_PATTERN)) {
                model.addAttribute("filterForm", filterForm);
                bindings.rejectValue("filterEmail", "error.filter.filterEmail");
                logger.info("error submit! error format for filterEmail: filterForm={}",
                    filterForm);
                return FILTER_JSP;
            }
            /** async filter and annovate variations. This operation will process data background and send results via email to user later. */
            filterService.asyncFilterAnnotateAndSendEmail(variations,
                VariationDataBaseType.of(filterForm.getVariationDb()),
                RemoveDispensableType.of(filterForm.getRemoveDispensable()),
                filterForm.getFilterEmail());
            redirectModel.addFlashAttribute("asyncSuccess", true);
            return "redirect:/annotate/async/result.htm";
        }

        List<Variation> filterResult = filterService.filterVariations(variations,
            VariationDataBaseType.of(filterForm.getVariationDb()));
        if (RemoveDispensableType
            .of(filterForm.getRemoveDispensable()) == RemoveDispensableType.YES) {
            filterResult = filterService.filterDispensableGeneVariations(variations);
        }
        Path vcfFilePath = annotateService.convertVariationsToVcfFile(filterResult);

        vcfFilePath = annotateService.annotateVcfFormatVariation(vcfFilePath);
        Path annovarInputPath = annovarUtils
            .getAnnovarInputPath(vcfFilePath.getFileName().toString());
        Path annotateResultPath = annovarUtils
            .getAnnotatePath(vcfFilePath.getFileName().toString());
        Path exonicAnnotatePath = annovarUtils
            .getExonicAnnotatePath(vcfFilePath.getFileName().toString());
        Path combineAnnovarOutPath = annovarUtils
            .getCombineAnnovarOutPath(vcfFilePath.getFileName().toString());
        Path annovarInvalidInputPath = annovarUtils
            .getAnnovarInvalidInputPath(vcfFilePath.getFileName().toString());

        redirectModel.addFlashAttribute("annovarInput", annovarInputPath.getFileName().toString());
        redirectModel.addFlashAttribute("annotateResult",
            annotateResultPath.getFileName().toString());
        redirectModel.addFlashAttribute("exonicAnnotateResult",
            exonicAnnotatePath.getFileName().toString());
        if (exonicAnnotatePath.toFile().length() > 0) {
            Path combinedAnnotateResultPath = null;
            try {
                combinedAnnotateResultPath = annotateService
                    .mergeAnnotateResult(vcfFilePath.getFileName().toString());
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
        return "redirect:/annotate/result.htm";
    }

    @RequestMapping(value = { "/filter/result.htm" }, method = { RequestMethod.GET })
    public String showFilterResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("filterResult")) {
            throw new InvalidAccessException("Invalid access!");
        }
        return FILTER_RESULT_JSP;
    }

    @RequestMapping(value = { "/filter/async/result.htm" }, method = { RequestMethod.GET })
    public String showAsyncFilterResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("asyncSuccess")) {
            throw new InvalidAccessException("Invalid access!");
        }
        return FILTER_ASYNC_RESULT_JSP;
    }

    @RequestMapping(value = { "/filter/result/{filename:.+}" }, method = { RequestMethod.GET })
    public ResponseEntity<byte[]> downloadFilterResult(HttpServletRequest request,
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

    private boolean validateFilterParams(HttpServletRequest request, @Valid FilterForm filterForm,
                                         BindingResult bindings, MultipartFile filterFile,
                                         Model model) {
        if (bindings.hasErrors()) {
            model.addAttribute("filterForm", filterForm);
            logger.info("error submit! FilterForm is empty: filterForm={}", filterForm);
            return false;
        }
        if (VariationDataBaseType.of(filterForm.getVariationDb()) == null) {
            logger.info("error submit! input format type required!" + getClientIP(request));
            model.addAttribute("filterForm", filterForm);
            bindings.rejectValue("variationDb", "error.filter.variationDb");
            return false;
        }
        if (FilterInputType.of(filterForm.getInputFormatType()) == null) {
            logger.info("error submit! variation database type required!" + getClientIP(request));
            model.addAttribute("filterForm", filterForm);
            bindings.rejectValue("inputFormatType", "error.filter.inputFormatType");
            return false;
        }
        if (RemoveDispensableType.of(filterForm.getRemoveDispensable()) == null) {
            logger.info(
                "error submit! remove dispensable gene type required!" + getClientIP(request));
            model.addAttribute("removeDispensable", filterForm);
            bindings.rejectValue("removeDispensable", "error.filter.removeDispensable");
            return false;
        }
        if (StringUtils.isBlank(filterForm.getFilterInput()) && filterFile.isEmpty()) {
            logger.info(
                "error submit! Both filterForm and filterFile are empty!" + getClientIP(request));
            model.addAttribute("filterForm", filterForm);
            bindings.rejectValue("filterInput", "error.filter.filterInput");
            return false;
        }
        return true;
    }

}
