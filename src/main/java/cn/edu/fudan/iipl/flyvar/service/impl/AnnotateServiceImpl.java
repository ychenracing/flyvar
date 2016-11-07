package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.CombineAnnotateResultException;
import cn.edu.fudan.iipl.flyvar.exception.CommandFailedException;
import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CommandExecutorService;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;

/**
 * @author racing
 * @version $Id: AnnovarUtils.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
@Service
public class AnnotateServiceImpl implements AnnotateService {

    private static final Logger     logger = LoggerFactory.getLogger(AnnotateServiceImpl.class);

    @Autowired
    private CommandExecutorService  commandExecutorService;

    @Autowired
    private ThreadPoolTaskExecutor  taskExecutor;

    @Autowired
    private FlyvarMailSenderService mailSenderService;

    @Autowired
    private AnnovarUtils            annovarUtils;

    @Autowired
    private PathUtils               pathUtils;

    @Override
    public Path annotateVcfFormatVariation(Path vcfFormatVariationPath) {
        Path annotationFolderPath = Paths.get(pathUtils.getFlyvarRoot(),
            annovarUtils.getAnnotationPath());
        if (!annotationFolderPath.equals(vcfFormatVariationPath.getParent())) {
            logger.error(
                "The file to annotate is not in the directory of annovationPath. variationFilePath={}",
                vcfFormatVariationPath);
            return null;
        }
        if (!vcfFormatVariationPath.toFile().exists()) {
            logger.error("The file to annotate is not exist. variationFilePath={}",
                vcfFormatVariationPath);
            return null;
        }
        commandExecutorService.executeSerially(
            annovarUtils.getAnnovarCommands(vcfFormatVariationPath.getFileName().toString()));
        return vcfFormatVariationPath;
    }

    private List<String> getVcfHeaderLines() {
        List<String> headerLines = null;
        try {
            headerLines = FileUtils.readLines(
                pathUtils.getAbsoluteAnnotationFilesPath().resolve("vcf.header").toFile(), "utf-8");
        } catch (IOException e) {
            logger.error("read vcf headers error!", e);
        }
        return headerLines == null ? Lists.newArrayList() : headerLines;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.AnnotateService#convertQueryResultVariationsToVcfFile(java.util.Collection)
     */
    @Override
    public Path convertQueryResultVariationsToVcfFile(Collection<QueryResultVariation> resultVariations) {
        List<String> vcfLines = QueryResultVariation.convertToVcfLines(resultVariations);
        Path vcfFilePath = pathUtils.getAbsoluteAnnotationFilesPath()
            .resolve(FlyvarFileUtils.getGeneratedFileName(null, null));
        try {
            List<String> lines = getVcfHeaderLines();
            lines.addAll(vcfLines);
            FileUtils.writeLines(vcfFilePath.toFile(), lines);
        } catch (IOException e) {
            logger.error("Write QueryResultVariation to vcf file error!", e);
            return null;
        }
        return vcfFilePath;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.AnnotateService#convertVariationsToVcfFile(java.util.Collection)
     */
    @Override
    public Path convertVariationsToVcfFile(Collection<Variation> variations) {
        List<String> vcfLines = Variation.convertToVcfLines(variations);
        Path vcfFilePath = pathUtils.getAbsoluteAnnotationFilesPath()
            .resolve(FlyvarFileUtils.getGeneratedFileName(null, null));
        try {
            List<String> lines = getVcfHeaderLines();
            lines.addAll(vcfLines);
            FileUtils.writeLines(vcfFilePath.toFile(), lines);
        } catch (IOException e) {
            logger.error("Write Variation to vcf file error!", e);
            return null;
        }
        return vcfFilePath;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.AnnotateService#mergeAnnotateResult(java.lang.String)
     */
    @Override
    public Path mergeAnnotateResult(String annovarInputFileName) {
        // problem exists in running R script. Cannot combine annotate result in some cases.
        Path combinedOutputPath = null;
        try {
            List<String> combineScriptLines = Lists.newArrayList();
            combineScriptLines
                .addAll(annovarUtils.getRscriptHeadersForCombiningAnnotate(annovarInputFileName));

            combineScriptLines.addAll(FileUtils.readLines(
                pathUtils.getAbsoluteAnnotationFilesPath().resolve("excuteR.R").toFile(), "utf-8"));
            combinedOutputPath = pathUtils.getAbsoluteAnnotationFilesPath().resolve(
                FlyvarFileUtils.getGeneratedFileName("combined_annotate_", ".variant_function"));
            combineScriptLines.add(annovarUtils
                .getRscriptBottomForCombiningAnnotate(combinedOutputPath.getFileName().toString()));
            Path rScriptPath = pathUtils.getAbsoluteAnnotationFilesPath()
                .resolve(FlyvarFileUtils.getGeneratedFileName("generated_r_", ".r"));
            FileUtils.writeLines(rScriptPath.toFile(), combineScriptLines);
            logger.info("write to r script success! rScriptPath={}", rScriptPath);
            Path rScriptOutputPath = pathUtils.getAbsoluteAnnotationFilesPath()
                .resolve(FlyvarFileUtils.getGeneratedFileName("generated_r_output_", ".txt"));

            String combineRunningCommand = annovarUtils.getRScriptCombineRunningCommand(
                rScriptPath.getFileName().toString(), rScriptOutputPath.getFileName().toString());
            try {
                commandExecutorService.execute(combineRunningCommand);
            } catch (CommandFailedException ex) {
                throw new CombineAnnotateResultException("combine annotate result error!", ex);
            }
        } catch (Exception ex) {
            logger.warn("combine annotate result error! ex={}", ex.getMessage());
            throw new CombineAnnotateResultException("combine annotate result error!");
        }
        return combinedOutputPath;
    }

    @Override
    public void asyncAnnotateVcfFormatVariation(Path vcfFormatVariationPath, String receiver) {
        taskExecutor.execute(() -> {
            Path vcfPath = annotateVcfFormatVariation(vcfFormatVariationPath);

            Path annovarInputPath = annovarUtils
                .getAnnovarInputPath(vcfPath.getFileName().toString());
            Path annotateResultPath = annovarUtils
                .getAnnotatePath(vcfPath.getFileName().toString());
            Path exonicAnnotatePath = annovarUtils
                .getExonicAnnotatePath(vcfPath.getFileName().toString());
            Path combineAnnovarOutPath = annovarUtils
                .getCombineAnnovarOutPath(vcfPath.getFileName().toString());
            Path annovarInvalidInputPath = annovarUtils
                .getAnnovarInvalidInputPath(vcfPath.getFileName().toString());

            Map<String, Object> emailParams = Maps.newHashMap();
            emailParams.put("annovarInput", annovarInputPath.getFileName().toString());
            emailParams.put("annotateResult", annotateResultPath.getFileName().toString());
            emailParams.put("exonicAnnotateResult", exonicAnnotatePath.getFileName().toString());
            if (exonicAnnotatePath.toFile().length() > 0) {
                Path combinedAnnotateResultPath = null;
                try {
                    combinedAnnotateResultPath = mergeAnnotateResult(
                        vcfPath.getFileName().toString());
                    emailParams.put("combinedExonicResult",
                        combinedAnnotateResultPath.getFileName().toString());
                } catch (CombineAnnotateResultException ex) {
                }
            }
            emailParams.put("combineAnnovarOut", combineAnnovarOutPath.getFileName().toString());
            if (annovarInvalidInputPath.toFile().exists()) {
                emailParams.put("annovarInvalidInput",
                    annovarInvalidInputPath.getFileName().toString());
            }

            mailSenderService.sendAnnotateResults(emailParams, receiver);
        });
    }

}
