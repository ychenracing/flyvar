package cn.edu.fudan.iipl.flyvar.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CommandExecutorService;

@Service
public class AnnotateServiceImpl implements AnnotateService {

    /** annotate目录在属性文件中的占位符名称，用于定位annotate目录并且用绝对路径替换占位符 */
    @Value("${annovar.scriptPlaceHolder}")
    private String                 scriptPlaceHolder;

    /** 运行annotate的目录，用于定位annotate的脚本文件和输入输出文件 */
    @Value("${annovar.annotationPath}")
    private String                 annotationPath;

    /** 运行annotate的输入文件的文件名，vcf格式 */
    @Value("${annovar.variationVcfPlaceHolder}")
    private String                 variationVcfPlaceHolder;

    @Value("${annovar.command1}")
    private String                 annovarCommand1;

    @Value("${annovar.command2}")
    private String                 annovarCommand2;

    @Value("${annovar.command3}")
    private String                 annovarCommand3;

    @Autowired
    private CommandExecutorService commandExecutorService;

    /**
     * 获取annovar运行命令，其中，所有文件和目录都使用绝对路径。命令配置在属性文件中，路径部分使用占位符，读取之后把占位符替换为真实路径。
     * @param annovarInputFileName annovar输入文件的文件名
     * @return
     */
    private List<String> getAnnotateCommands(String annovarInputFileName) {
        Path flyvarRootPath = Paths.get(FlyvarFileUtils.getFlyvarRoot()).toAbsolutePath();
        List<String> commands = Lists.newArrayList(annovarCommand1, annovarCommand2,
            annovarCommand3);
        return commands.stream()
            .map(command -> command
                .replaceAll(scriptPlaceHolder,
                    flyvarRootPath.resolveSibling(annotationPath).toAbsolutePath().toString())
                .replaceAll(variationVcfPlaceHolder, annovarInputFileName))
            .collect(Collectors.toList());
    }

    @Override
    public Path annotateVariationVcfFormat(Path variationPath) {
        commandExecutorService
            .executeSerially(getAnnotateCommands(variationPath.getFileName().toString()));
        return variationPath;
    }

}
