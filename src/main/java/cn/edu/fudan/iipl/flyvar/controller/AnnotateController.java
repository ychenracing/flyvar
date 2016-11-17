/**
 * ychen. Copyright (c) 2016年11月12日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.FlyvarSystemException;
import cn.edu.fudan.iipl.flyvar.exception.NotFoundException;
import cn.edu.fudan.iipl.flyvar.form.AnnotateForm;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.AnnotateInputType;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;

/**
 * Annotate控制器
 * 
 * @author racing
 * @version $Id: AnnotateController.java, v 0.1 2016年11月12日 下午4:50:41 racing Exp $
 */
@Controller
public class AnnotateController extends AbstractController {

    private static final Logger logger              = LoggerFactory
        .getLogger(AnnotateController.class);

    private static final String ANNOTATE_JSP        = "annotate/annotate";

    private static final String ANNOTATE_RESULT_JSP = "annotate/result";

    @Autowired
    private PathUtils           pathUtils;

    @Autowired
    private AnnovarUtils        annovarUtils;

    @Autowired
    private AnnotateService     annotateService;

    @RequestMapping(value = { "/annotate.htm" }, method = { RequestMethod.GET })
    public String showAnnotate(Model model) {
        model.addAttribute("annotateForm", new AnnotateForm());
        return ANNOTATE_JSP;
    }

    @RequestMapping(value = { "/annotate/result.htm" }, method = { RequestMethod.GET })
    public String annotateResult(HttpServletRequest request, Model model) {
        checkReferer(request);
        if (!model.containsAttribute("annovarInput") || !model.containsAttribute("annotateResult")
            || !model.containsAttribute("exonicAnnotate")
            || !model.containsAttribute("combineAnnovarOut")) {
            throw new FlyvarSystemException("Invalid access!");
        }
        return ANNOTATE_RESULT_JSP;
    }

    @RequestMapping(value = { "/annotate/result/{filename:.+}" }, method = { RequestMethod.GET })
    public ResponseEntity<byte[]> downloadAnnotateResult(HttpServletRequest request,
                                                         HttpServletResponse response, Model model,
                                                         @PathVariable("filename") String filename) throws IOException {
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
            logger.info("mimetype is not detectable, will take default. mimeType={}", mimeType);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", filePath.getFileName().toString());
        logger.info("download file! ip={}, filePath={}", getClientIP(request), filePath);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath.toFile()), headers,
            HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/annotate/annotate.htm" }, method = { RequestMethod.POST })
    public String doAnnotate(HttpServletRequest request, @Valid AnnotateForm annotateForm,
                             BindingResult bindings, MultipartFile annotateFile,
                             RedirectAttributes redirectModel, Model model) {
        if (!validateAnnotateParams(request, annotateForm, bindings, annotateFile, redirectModel,
            model)) {
            return ANNOTATE_JSP;
        }

        AnnotateInputType inputFormat = AnnotateInputType.of(annotateForm.getInputFormatType());
        if (inputFormat == null) {
            model.addAttribute("annotateForm", annotateForm);
            bindings.rejectValue("inputFormatType", "{error.annotate.inputFormatType}");
            return ANNOTATE_JSP;
        }

        Path vcfFilePath = null;

        if (inputFormat == AnnotateInputType.FOUR_COLUMN_TAB_SEPARATED) {
            String variationStr = annotateForm.getAnnotateInput();
            if (StringUtils.isBlank(variationStr)) {
                variationStr = FlyvarFileUtils.readFileToStringDiscardHeader(
                    FlyvarFileUtils.saveUploadFileAndGetFilePath(annotateFile,
                        pathUtils.getAbsoluteUploadFilesPath().toString()));
            }
            Set<Variation> variations = Variation.convertInputToVariations(variationStr);
            if (CollectionUtils.isEmpty(variations)) {
                model.addAttribute("annotateForm", annotateForm);
                bindings.rejectValue("annotateInput", "error.annotate.annotateInputFormat");
                logger.info(
                    "error submit! error format for variation input or file: annotateForm={}",
                    annotateForm);
                return ANNOTATE_JSP;
            }
            vcfFilePath = annotateService.convertVariationsToVcfFile(variations);
        } else { // vcf format
            if (annotateFile != null) {
                vcfFilePath = FlyvarFileUtils.saveUploadFileAndGetFilePath(annotateFile,
                    pathUtils.getAbsoluteAnnotationFilesPath().toString());
            } else {
                String variationVcfStr = annotateForm.getAnnotateInput();
                List<String> vcfLines = Lists
                    .newArrayList(variationVcfStr.replaceAll("\r", "").split("\n"));
                vcfFilePath = pathUtils.getAbsoluteAnnotationFilesPath()
                    .resolve(FlyvarFileUtils.getGeneratedFileName("Annotate_uploaded_", ".vcf"));
                FlyvarFileUtils.writeLinesToPath(vcfLines, vcfFilePath.toString());
            }
        }
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

    private boolean validateAnnotateParams(HttpServletRequest request,
                                           @Valid AnnotateForm annotateForm, BindingResult bindings,
                                           MultipartFile annotateFile,
                                           RedirectAttributes redirectModel, Model model) {
        if (bindings.hasErrors()) {
            model.addAttribute("annotateForm", annotateForm);
            logger.info("error submit! AnnotateForm is empty: annotateForm={}", annotateForm);
            return false;
        }
        if (StringUtils.isBlank(annotateForm.getAnnotateInput()) && annotateFile.isEmpty()) {
            logger.info("error submit! Both annotateForm and annotateFile are empty!"
                        + getClientIP(request));
            model.addAttribute("annotateForm", annotateForm);
            bindings.rejectValue("annotateInput", "error.annotate.annotateInput");
            return false;
        }
        return true;
    }
}
