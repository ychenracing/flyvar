package cn.edu.fudan.iipl.flyvar.scheduler;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.DateUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;

@Service
public class AnnotateFileScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateFileScheduler.class);

    @Autowired
    private PathUtils           pathUtils;

    @Autowired
    private AnnovarUtils        annovarUtils;

    public void doTask() {
        clean30DaysFiles();
    }

    /**
     * delete annotate files over past 30 days.
     */
    public void clean30DaysFiles() {
        Path annotationPath = pathUtils.getAbsoluteAnnotationFilesPath();
        String annotateFileGlob = "*{" + StringUtils.join(
            Arrays.asList(annovarUtils.getAnnovarInputSuffix(), annovarUtils.getAnnotateSuffix(),
                annovarUtils.getExonicAnnotateSuffix(), annovarUtils.getCombineAnnovarOutSuffix(),
                annovarUtils.getInvalidInputSuffix()),
            ",") + "}";
        FileTime past30Day = FileTime
            .fromMillis(DateUtils.addDay(DateUtils.current(), -30).getTime());

        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(annotationPath, annotateFileGlob);
            ds.forEach(path -> {
                try {
                    if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)
                        && Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS)
                            .compareTo(past30Day) < 0) {
                        Files.delete(path);
                        logger.info("Delete file successfully! path={}", path);
                    }
                } catch (IOException ex) {
                    logger.error("Get last modified time error! path=" + path, ex);
                }
            });
        } catch (IOException ex) {
            logger.error("get filter files error! path=" + annotationPath, ex);
        }
    }
}
