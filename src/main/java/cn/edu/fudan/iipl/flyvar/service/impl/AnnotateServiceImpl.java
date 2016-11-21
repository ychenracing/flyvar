package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CommandExecutorService;

/**
 * @author racing
 * @version $Id: AnnovarUtils.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
@Service
public class AnnotateServiceImpl implements AnnotateService {

    private static final Logger    logger = LoggerFactory.getLogger(AnnotateServiceImpl.class);

    @Autowired
    private CommandExecutorService commandExecutorService;

    @Autowired
    private AnnovarUtils           annovarUtils;

    @Autowired
    private PathUtils              pathUtils;

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
     * @see cn.edu.fudan.iipl.flyvar.service.AnnotateService#mergeAnnotateResult(java.nio.file.Path)
     */
    @Override
    public Path mergeAnnotateResult(String annovarInputFileName) {
        // TODO: problem exists in running R script.
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
            commandExecutorService.execute(combineRunningCommand);
        } catch (Exception ex) {
            logger.error("combine annotate result error!", ex);
        }
        return combinedOutputPath;
    }

}
