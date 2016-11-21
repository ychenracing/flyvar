package cn.edu.fudan.iipl.flyvar.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * annovar命令工具类。获取annovar命令时，
 * <ul>
 * <li>1. 需要将annovarCommand中annotationPathPlaceholder的内容替换为annotationPath的内容；</li>
 * <li>2.
 * 需要将annovarCommand中variationVcfPlaceholder替换为vcf格式的variation文件的文件名，该文件必须位于annotationPath路径下；</li>
 * <li>3. 需要将annovarCommand中annovarInputSuffixPlaceholder替换为annovarInputSuffix的内容；</li>
 * <li>4. 需要将annovarCommand中annotateSuffixPlaceholder替换为annotateSuffix的内容；</li>
 * <li>5. 需要将annovarCommand中exonicAnnotateSuffixPlaceholder替换为exonicAnnotateSuffix的内容；</li>
 * <li>6. 需要将annovarCommand中combineAnnovarOutSuffixPlaceholder替换为combineAnnovarOutSuffix的内容；</li>
 * </ul>
 * 
 * @author racing
 * @version $Id: AnnovarUtils.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
@Component
public class AnnovarUtils {

    private static final Logger logger = LoggerFactory.getLogger(AnnovarUtils.class);

    /** annotate目录在属性文件中的占位符名称，用于定位annotate目录并且用绝对路径替换占位符 */
    @Value("${annovar.annotationPathPlaceholder}")
    private String              annotationPathPlaceholder;

    /** 运行annotate的目录，用于定位annotate的脚本文件和输入输出文件 */
    @Value("${annovar.annotationPath}")
    private String              annotationPath;

    /** 运行annotate的输入文件的文件名，vcf格式 */
    @Value("${annovar.variationVcfPlaceholder}")
    private String              variationVcfPlaceholder;

    @Value("${annovar.command1}")
    private String              annovarCommand1;

    @Value("${annovar.command2}")
    private String              annovarCommand2;

    @Value("${annovar.command3}")
    private String              annovarCommand3;

    @Value("${annovar.annovarInputSuffixPlaceholder}")
    private String              annovarInputSuffixPlaceholder;

    @Value("${annovar.annovarInputSuffix}")
    private String              annovarInputSuffix;

    @Value("${annovar.annotateSuffixPlaceholder}")
    private String              annotateSuffixPlaceholder;

    @Value("${annovar.annotateSuffix}")
    private String              annotateSuffix;

    @Value("${annovar.exonicAnnotateSuffixPlaceholder}")
    private String              exonicAnnotateSuffixPlaceholder;

    @Value("${annovar.exonicAnnotateSuffix}")
    private String              exonicAnnotateSuffix;

    @Value("${annovar.combineAnnovarOutSuffixPlaceholder}")
    private String              combineAnnovarOutSuffixPlaceholder;

    @Value("${annovar.combineAnnovarOutSuffix}")
    private String              combineAnnovarOutSuffix;

    @Value("${annovar.invalidInputSuffix}")
    private String              invalidInputSuffix;

    @Value("${combine.header.exonAnnovateResultPlaceholder}")
    private String              combineHeaderExonAnnovateResultPlaceholder;
    @Value("${combine.header.exonAnnovateResult}")
    private String              combineHeaderExonAnnovateResult;
    @Value("${combine.header.annovateResultPlaceholder}")
    private String              combineHeaderAnnovateResultPlaceholder;
    @Value("${combine.header.annovateResult}")
    private String              combineHeaderAnnovateResult;
    @Value("${combine.bottom.combinedOutputPlaceholder}")
    private String              combineBottomCombinedOutputPlaceholder;
    @Value("${combine.bottom}")
    private String              combineBottom;
    @Value("${combine.r.command.rScriptPathPlaceholder}")
    private String              combineRCommandRScriptPathPlaceholder;
    @Value("${combine.r.command.rScriptOutputPlaceholder}")
    private String              combineRCommandRScriptOutputPlaceholder;
    @Value("${combine.r.command}")
    private String              combineRCommand;

    @Autowired
    private PathUtils           pathUtils;

    public String getCombineHeaderExonAnnovateResultPlaceholder() {
        return combineHeaderExonAnnovateResultPlaceholder;
    }

    public void setCombineHeaderExonAnnovateResultPlaceholder(String combineHeaderExonAnnovateResultPlaceholder) {
        this.combineHeaderExonAnnovateResultPlaceholder = combineHeaderExonAnnovateResultPlaceholder;
    }

    public String getCombineHeaderExonAnnovateResult() {
        return combineHeaderExonAnnovateResult;
    }

    public void setCombineHeaderExonAnnovateResult(String combineHeaderExonAnnovateResult) {
        this.combineHeaderExonAnnovateResult = combineHeaderExonAnnovateResult;
    }

    public String getCombineHeaderAnnovateResultPlaceholder() {
        return combineHeaderAnnovateResultPlaceholder;
    }

    public void setCombineHeaderAnnovateResultPlaceholder(String combineHeaderAnnovateResultPlaceholder) {
        this.combineHeaderAnnovateResultPlaceholder = combineHeaderAnnovateResultPlaceholder;
    }

    public String getCombineHeaderAnnovateResult() {
        return combineHeaderAnnovateResult;
    }

    public void setCombineHeaderAnnovateResult(String combineHeaderAnnovateResult) {
        this.combineHeaderAnnovateResult = combineHeaderAnnovateResult;
    }

    public String getCombineBottomCombinedOutputPlaceholder() {
        return combineBottomCombinedOutputPlaceholder;
    }

    public void setCombineBottomCombinedOutputPlaceholder(String combineBottomCombinedOutputPlaceholder) {
        this.combineBottomCombinedOutputPlaceholder = combineBottomCombinedOutputPlaceholder;
    }

    public String getCombineBottom() {
        return combineBottom;
    }

    public void setCombineBottom(String combineBottom) {
        this.combineBottom = combineBottom;
    }

    public String getCombineRCommandRScriptPathPlaceholder() {
        return combineRCommandRScriptPathPlaceholder;
    }

    public void setCombineRCommandRScriptPathPlaceholder(String combineRCommandRScriptPathPlaceholder) {
        this.combineRCommandRScriptPathPlaceholder = combineRCommandRScriptPathPlaceholder;
    }

    public String getCombineRCommandRScriptOutputPlaceholder() {
        return combineRCommandRScriptOutputPlaceholder;
    }

    public void setCombineRCommandRScriptOutputPlaceholder(String combineRCommandRScriptOutputPlaceholder) {
        this.combineRCommandRScriptOutputPlaceholder = combineRCommandRScriptOutputPlaceholder;
    }

    public String getCombineRCommand() {
        return combineRCommand;
    }

    public void setCombineRCommand(String combineRCommand) {
        this.combineRCommand = combineRCommand;
    }

    /**
     * 获取annovar命令。 需要进行annovate的文件必须位于annotatePath目录下。
     * 其中，所有文件和目录都使用绝对路径。命令配置在属性文件中，路径部分使用占位符，读取之后把占位符替换为真实路径。
     * 
     * @param annovarInputFileName
     * @return
     */
    public List<String> getAnnovarCommands(String annovarInputFileName) {

        List<String> commands = Lists.newArrayList(getAnnovarCommand1(), getAnnovarCommand2(),
            getAnnovarCommand3());
        return commands.stream().map(replaceCommandPlaceholder(annovarInputFileName))
            .collect(Collectors.toList());
    }

    private Function<String, String> replaceCommandPlaceholder(String annovarInputFileName) {
        return command -> {
            String returnedCommand = command
                .replaceAll(getAnnotationPathPlaceholder(),
                    Paths.get(pathUtils.getFlyvarRoot(), getAnnotationPath()).toAbsolutePath()
                        .toString().replaceAll("\\\\", "/"))
                .replaceAll(getVariationVcfPlaceholder(), annovarInputFileName)
                .replaceAll(getAnnovarInputSuffixPlaceholder(), getAnnovarInputSuffix())
                .replaceAll(getAnnotateSuffixPlaceholder(), getAnnotateSuffix())
                .replaceAll(getExonicAnnotateSuffixPlaceholder(), getExonicAnnotateSuffix())
                .replaceAll(getCombineAnnovarOutSuffixPlaceholder(), getCombineAnnovarOutSuffix());
            return returnedCommand;
        };
    }

    public String getAnnotationPathPlaceholder() {
        return annotationPathPlaceholder;
    }

    public void setAnnotationPathPlaceholder(String annotationPathPlaceholder) {
        this.annotationPathPlaceholder = annotationPathPlaceholder;
    }

    public String getAnnotationPath() {
        return annotationPath;
    }

    public void setAnnotationPath(String annotationPath) {
        this.annotationPath = annotationPath;
    }

    public String getVariationVcfPlaceholder() {
        return variationVcfPlaceholder;
    }

    public void setVariationVcfPlaceholder(String variationVcfPlaceholder) {
        this.variationVcfPlaceholder = variationVcfPlaceholder;
    }

    public String getAnnovarCommand1() {
        return annovarCommand1;
    }

    public void setAnnovarCommand1(String annovarCommand1) {
        this.annovarCommand1 = annovarCommand1;
    }

    public String getAnnovarCommand2() {
        return annovarCommand2;
    }

    public void setAnnovarCommand2(String annovarCommand2) {
        this.annovarCommand2 = annovarCommand2;
    }

    public String getAnnovarCommand3() {
        return annovarCommand3;
    }

    public void setAnnovarCommand3(String annovarCommand3) {
        this.annovarCommand3 = annovarCommand3;
    }

    public String getAnnovarInputSuffixPlaceholder() {
        return annovarInputSuffixPlaceholder;
    }

    public void setAnnovarInputSuffixPlaceholder(String annovarInputSuffixPlaceholder) {
        this.annovarInputSuffixPlaceholder = annovarInputSuffixPlaceholder;
    }

    public String getAnnovarInputSuffix() {
        return annovarInputSuffix;
    }

    public void setAnnovarInputSuffix(String annovarInputSuffix) {
        this.annovarInputSuffix = annovarInputSuffix;
    }

    /**
     * Get the path to annovar input file by the variation file name being annotated. Being
     * annovated file must be put in the directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public Path getAnnovarInputPath(String annovarInputFileName) {
        return Paths.get(pathUtils.getFlyvarRoot(), getAnnotationPath(),
            annovarInputFileName + getAnnovarInputSuffix());
    }

    public String getAnnotateSuffixPlaceholder() {
        return annotateSuffixPlaceholder;
    }

    public void setAnnotateSuffixPlaceholder(String annotateSuffixPlaceholder) {
        this.annotateSuffixPlaceholder = annotateSuffixPlaceholder;
    }

    public String getAnnotateSuffix() {
        return annotateSuffix;
    }

    public void setAnnotateSuffix(String annotateSuffix) {
        this.annotateSuffix = annotateSuffix;
    }

    /**
     * Get the path to variation function file by the variation file name being annotated. Being
     * annovated file must be put in the directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public Path getAnnotatePath(String annovarInputFileName) {
        return Paths
            .get(getAnnovarInputPath(annovarInputFileName).toString() + getAnnotateSuffix());
    }

    public String getExonicAnnotateSuffixPlaceholder() {
        return exonicAnnotateSuffixPlaceholder;
    }

    public void setExonicAnnotateSuffixPlaceholder(String exonicAnnotateSuffixPlaceholder) {
        this.exonicAnnotateSuffixPlaceholder = exonicAnnotateSuffixPlaceholder;
    }

    public String getExonicAnnotateSuffix() {
        return exonicAnnotateSuffix;
    }

    /**
     * Get the path to exonic variation function file by the variation file name being annotated.
     * Being annovated file must be put in the directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public Path getExonicAnnotatePath(String annovarInputFileName) {
        return Paths.get(getAnnovarInputPath(annovarInputFileName) + getExonicAnnotateSuffix());
    }

    public void setExonicAnnotateSuffix(String exonicAnnotateSuffix) {
        this.exonicAnnotateSuffix = exonicAnnotateSuffix;
    }

    public String getCombineAnnovarOutSuffixPlaceholder() {
        return combineAnnovarOutSuffixPlaceholder;
    }

    public void setCombineAnnovarOutSuffixPlaceholder(String combineAnnovarOutSuffixPlaceholder) {
        this.combineAnnovarOutSuffixPlaceholder = combineAnnovarOutSuffixPlaceholder;
    }

    public String getCombineAnnovarOutSuffix() {
        return combineAnnovarOutSuffix;
    }

    /**
     * Get the path to combine annovar output file by the variation file name being annotated. Being
     * annovated file must be put in the directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public Path getCombineAnnovarOutPath(String annovarInputFileName) {
        return Paths.get(pathUtils.getFlyvarRoot(), getAnnotationPath(),
            annovarInputFileName + getCombineAnnovarOutSuffix());
    }

    public void setCombineAnnovarOutSuffix(String combineAnnovarOutSuffix) {
        this.combineAnnovarOutSuffix = combineAnnovarOutSuffix;
    }

    /**
     * Get the path to annovar invalid input file by the variation file name being annotated. Being
     * annovated file must be put in the directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public Path getAnnovarInvalidInputPath(String annovarInputFileName) {
        return Paths.get(getAnnovarInputPath(annovarInputFileName) + getInvalidInputSuffix());
    }

    public String getInvalidInputSuffix() {
        return invalidInputSuffix;
    }

    public void setInvalidInputSuffix(String invalidInputSuffix) {
        this.invalidInputSuffix = invalidInputSuffix;
    }

    /**
     * Get R script headers for combining annotate results. annovarInputFile must be put in the
     * directory of annovar's annotationPath.
     * 
     * @param annovarInputFileName
     * @return
     */
    public List<String> getRscriptHeadersForCombiningAnnotate(String annovarInputFileName) {
        String header1 = getCombineHeaderExonAnnovateResult().replaceAll(
            getCombineHeaderExonAnnovateResultPlaceholder(),
            getExonicAnnotatePath(annovarInputFileName).toString());
        String header2 = getCombineHeaderAnnovateResult().replaceAll(
            getCombineHeaderAnnovateResultPlaceholder(),
            getAnnotatePath(annovarInputFileName).toString());
        return Lists.newArrayList(header1, header2);
    }

    /**
     * Get R script bottoms for combining annotate results. combineOutputFile must be put in the
     * directory of annovar's annotationPath.
     * 
     * @param combineOutputFileName
     * @return
     */
    public String getRscriptBottomForCombiningAnnotate(String combineOutputFileName) {
        return getCombineBottom().replaceAll(getCombineBottomCombinedOutputPlaceholder(),
            pathUtils.getAbsoluteAnnotationFilesPath().resolve(combineOutputFileName).toString());
    }

    /**
     * Get R script running command for combining annotate results. rScriptFile and
     * rScriptOutputFile must be put in the directory of annovar's annotationPath.
     * 
     * @param rScriptFileName
     * @param rScriptOutputFileName
     * @return
     */
    public String getRScriptCombineRunningCommand(String rScriptFileName,
                                                  String rScriptOutputFileName) {
        if (!Files.exists(pathUtils.getAbsoluteAnnotationFilesPath().resolve(rScriptFileName))) {
            logger.error(rScriptFileName + " not exists in folder:"
                         + pathUtils.getAbsoluteAnnotationFilesPath());
        }
        return getCombineRCommand()
            .replaceAll(getCombineRCommandRScriptPathPlaceholder(),
                pathUtils.getAbsoluteAnnotationFilesPath().resolve(rScriptFileName).toString())
            .replaceAll(getCombineRCommandRScriptOutputPlaceholder(), pathUtils
                .getAbsoluteAnnotationFilesPath().resolve(rScriptOutputFileName).toString());
    }

}
