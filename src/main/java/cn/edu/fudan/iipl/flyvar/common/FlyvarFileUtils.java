package cn.edu.fudan.iipl.flyvar.common;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.fudan.iipl.flyvar.model.Variation;

public class FlyvarFileUtils {

    private static final Logger logger                     = LoggerFactory
        .getLogger(FlyvarFileUtils.class);

    private static final String GENERATED_FILE_NAME_PREFIX = "generated_file_";

    private static final String DEFAULT_GENERATED_FILE_EXT = ".txt";

    /**
     * 读取用户的输入文件到字符串，丢弃以'#'开头的那些header信息
     * 
     * @return
     */
    public static String readFileToStringDiscardHeader(Path filePath) {
        if (!filePath.toFile().exists()) {
            logger.warn("file not exists! cannot read file to String discard header! filePath={}",
                filePath);
            return null;
        }
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(filePath.toFile(), "utf-8");
        } catch (IOException ex) {
            logger.error("文件读取失败！filePath=" + filePath, ex);
        }
        return StringUtils.join(
            lines.stream().filter(line -> !line.startsWith("#")).collect(Collectors.toList()),
            "\n");
    }

    public static Path saveUploadFileAndGetFilePath(MultipartFile file, String uploadPath) {
        String originName = file.getOriginalFilename();
        String baseName = FilenameUtils.getBaseName(originName);
        String extName = FilenameUtils.getExtension(originName);
        StringBuilder newFileName = new StringBuilder(baseName).append("_")
            .append(DateUtils.formatFilename(DateUtils.current())).append("_")
            .append(System.currentTimeMillis()).append("_").append(RandomUtils.nextInt(0, 1000))
            .append("_").append(RandomUtils.nextInt(0, 1000)).append("_")
            .append(RandomUtils.nextInt(0, 1000)).append(".").append(extName);
        Path savePath = Paths.get(uploadPath, newFileName.toString());
        try {
            file.transferTo(savePath.toFile());
        } catch (IllegalStateException | IOException ex) {
            logger.error("save uploaded file error! path=" + savePath, ex);
        }
        logger.info("file uploaded! path={}", savePath);
        return savePath;
    }

    public static Path writeLinesToPath(List<String> lines, String _path) {
        return writeLinesToPath(lines, Paths.get(_path));
    }

    public static Path writeLinesToPath(List<String> lines, Path path) {
        try {
            FileUtils.writeLines(path.toFile(), lines);
        } catch (IOException ex) {
            logger.error("write lines to file error! path=" + path, ex);
        }
        return path;
    }

    public static Path writeVariationsToFile(Path outputPath, Collection<Variation> variations) {
        String filename = getGeneratedFileName("variations_", ".txt");
        Path filepath = outputPath.resolve(filename);
        writeLinesToPath(variations.stream().map(variation -> {
            return variation.getChr() + "\t" + variation.getPos() + "\t" + variation.getRef() + "\t"
                   + variation.getAlt();
        }).collect(Collectors.toList()), filepath);
        return outputPath;
    }

    public static String getGeneratedFileName(String prefix, String ext) {
        return new StringBuilder(StringUtils.isBlank(prefix) ? "" : prefix)
            .append(GENERATED_FILE_NAME_PREFIX)
            .append(DateUtils.formatFilename(DateUtils.current())).append("_")
            .append(System.currentTimeMillis()).append("_").append(RandomUtils.nextInt(0, 1000))
            .append(RandomUtils.nextInt(0, 1000)).append(RandomUtils.nextInt(0, 1000))
            .append(StringUtils.isBlank(ext) ? DEFAULT_GENERATED_FILE_EXT : ext).toString();
    }

}
