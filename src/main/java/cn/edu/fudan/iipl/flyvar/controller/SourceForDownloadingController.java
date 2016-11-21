/**
 * ychen. Copyright (c) 2016年11月20日.
 */
package cn.edu.fudan.iipl.flyvar.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.edu.fudan.iipl.flyvar.AbstractController;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.exception.NotFoundException;

/**
 * source for downloading controller
 * 
 * @author racing
 * @version $Id: SourceForDownloadingController.java, v 0.1 2016年11月20日 下午2:14:58 racing Exp $
 */
@Controller
public class SourceForDownloadingController extends AbstractController {
    private static final Logger logger                     = LoggerFactory
        .getLogger(SourceForDownloadingController.class);

    private static final String SOURCE_FOR_DOWNLOADING_JSP = "source/sourceForDownloading";

    @Autowired
    private PathUtils           pathUtils;

    @RequestMapping(value = { "/source.htm" }, method = { RequestMethod.GET })
    public String showSourceForDownloadingPage() {
        return SOURCE_FOR_DOWNLOADING_JSP;
    }

    @RequestMapping(value = { "source/{filename:.+}" }, method = { RequestMethod.GET })
    public ResponseEntity<byte[]> downloadSourceFile(HttpServletRequest request,
                                                     @PathVariable String filename) throws IOException {
        Path sourcePath = pathUtils.getAbsoluteSourceForDownloadingPath().resolve(filename);
        if (!Files.exists(sourcePath)) {
            throw new NotFoundException();
        }
        String mimeType = URLConnection
            .guessContentTypeFromName(sourcePath.getFileName().toString());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment", sourcePath.getFileName().toString());
        logger.info("download file! ip={}, filePath={}", getClientIP(request), sourcePath);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(sourcePath.toFile()), headers,
            HttpStatus.CREATED);
    }
}
