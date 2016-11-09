package cn.edu.fudan.iipl.flyvar.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * annovar命令工具类。获取annovar命令时，
 * <ul>
 * <li>1. 需要将annovarCommand中annotationPathPlaceholder的内容替换为annotationPath的内容；</li>
 * <li>2. 需要将annovarCommand中variationVcfPlaceholder替换为vcf格式的variation文件的文件名，该文件必须位于annotationPath路径下；</li>
 * <li>3. 需要将annovarCommand中annovarInputSuffixPlaceholder替换为annovarInputSuffix的内容；</li>
 * <li>4. 需要将annovarCommand中annotateSuffixPlaceholder替换为annotateSuffix的内容；</li>
 * <li>5. 需要将annovarCommand中exonicAnnotateSuffixPlaceholder替换为exonicAnnotateSuffix的内容；</li>
 * <li>6. 需要将annovarCommand中combineAnnovarOutSuffixPlaceholder替换为combineAnnovarOutSuffix的内容；</li>
 * </ul>
 * @author racing
 * @version $Id: AnnovarUtils.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
@Component
public class AnnovarUtils {

    /** annotate目录在属性文件中的占位符名称，用于定位annotate目录并且用绝对路径替换占位符 */
    @Value("${annovar.annotationPathPlaceholder}")
    private String    annotationPathPlaceholder;

    /** 运行annotate的目录，用于定位annotate的脚本文件和输入输出文件 */
    @Value("${annovar.annotationPath}")
    private String    annotationPath;

    /** 运行annotate的输入文件的文件名，vcf格式 */
    @Value("${annovar.variationVcfPlaceholder}")
    private String    variationVcfPlaceholder;

    @Value("${annovar.command1}")
    private String    annovarCommand1;

    @Value("${annovar.command2}")
    private String    annovarCommand2;

    @Value("${annovar.command3}")
    private String    annovarCommand3;

    @Value("${annovar.annovarInputSuffixPlaceholder}")
    private String    annovarInputSuffixPlaceholder;

    @Value("${annovar.annovarInputSuffix}")
    private String    annovarInputSuffix;

    @Value("${annovar.annotateSuffixPlaceholder}")
    private String    annotateSuffixPlaceholder;

    @Value("${annovar.annotateSuffix}")
    private String    annotateSuffix;

    @Value("${annovar.exonicAnnotateSuffixPlaceholder}")
    private String    exonicAnnotateSuffixPlaceholder;

    @Value("${annovar.exonicAnnotateSuffix}")
    private String    exonicAnnotateSuffix;

    @Value("${annovar.combineAnnovarOutSuffixPlaceholder}")
    private String    combineAnnovarOutSuffixPlaceholder;

    @Value("${annovar.combineAnnovarOutSuffix}")
    private String    combineAnnovarOutSuffix;

    @Autowired
    private PathUtils pathUtils;

    /**
     * 获取annovar命令。其中，所有文件和目录都使用绝对路径。命令配置在属性文件中，路径部分使用占位符，读取之后把占位符替换为真实路径。
     * @param annovarInputFileName
     * @return
     */
    public List<String> getAnnovarCommands(String annovarInputFileName) {
        Path flyvarRootPath = Paths.get(pathUtils.getFlyvarRoot()).toAbsolutePath();
        List<String> commands = Lists.newArrayList(getAnnovarCommand1(), getAnnovarCommand2(),
            getAnnovarCommand3());
        return commands.stream()
            .map(
                command -> command
                    .replaceAll(getAnnotationPathPlaceholder(),
                        flyvarRootPath.resolveSibling(getAnnotationPath()).toAbsolutePath()
                            .toString())
                    .replaceAll(getVariationVcfPlaceholder(), annovarInputFileName)
                    .replaceAll(getAnnovarInputSuffixPlaceholder(), getAnnovarInputSuffix())
                    .replaceAll(getAnnotateSuffixPlaceholder(), getAnnotateSuffix())
                    .replaceAll(getExonicAnnotateSuffixPlaceholder(), getExonicAnnotateSuffix())
                    .replaceAll(getCombineAnnovarOutSuffixPlaceholder(),
                        getCombineAnnovarOutSuffix()))
            .collect(Collectors.toList());
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

    public String getExonicAnnotateSuffixPlaceholder() {
        return exonicAnnotateSuffixPlaceholder;
    }

    public void setExonicAnnotateSuffixPlaceholder(String exonicAnnotateSuffixPlaceholder) {
        this.exonicAnnotateSuffixPlaceholder = exonicAnnotateSuffixPlaceholder;
    }

    public String getExonicAnnotateSuffix() {
        return exonicAnnotateSuffix;
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

    public void setCombineAnnovarOutSuffix(String combineAnnovarOutSuffix) {
        this.combineAnnovarOutSuffix = combineAnnovarOutSuffix;
    }

}
