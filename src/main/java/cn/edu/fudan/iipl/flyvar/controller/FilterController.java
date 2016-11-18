/**
 * ychen. Copyright (c) 2016年11月13日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
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

    private static final Logger logger            = LoggerFactory.getLogger(FilterController.class);

    private static final String FILTER_JSP        = "filter/filter";

    private static final String FILTER_RESULT_JSP = "filter/result";

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
    public String doQuery(HttpServletRequest request, @Valid FilterForm filterForm,
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
        List<Variation> filterResult = filterService.filterVariations(variations,
            VariationDataBaseType.of(filterForm.getVariationDb()));
        if (RemoveDispensableType
            .of(filterForm.getRemoveDispensable()) == RemoveDispensableType.YES) {
            filterResult = filterService.filterDispensableGeneVariations(variations);
        }
        Path vcfFilePath = annotateService.convertVariationsToVcfFile(filterResult);
        annotateService.annotateVcfFormatVariation(vcfFilePath);
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
        redirectModel.addFlashAttribute("exonicAnnotate",
            exonicAnnotatePath.getFileName().toString());
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
            throw new RuntimeException("Invalid access!");
        }
        return FILTER_RESULT_JSP;
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
