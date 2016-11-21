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
 * dispensable gene controller
 * 
 * @author racing
 * @version $Id: DispensableGeneController.java, v 0.1 2016年11月20日 上午11:23:44 racing Exp $
 */
@Controller
public class DispensableGeneController extends AbstractController {

    private static final Logger logger               = LoggerFactory
        .getLogger(DispensableGeneController.class);

    private static final String DISPENSABLE_GENE_JSP = "dispensable/dispensableGene";

    @Autowired
    private PathUtils           pathUtils;

    @RequestMapping(value = { "/dispensable.htm" }, method = { RequestMethod.GET })
    public String showDispensableGenePage() {
        return DISPENSABLE_GENE_JSP;
    }

    @RequestMapping(value = { "dispensable/{filename:.+}" }, method = { RequestMethod.GET })
    public ResponseEntity<byte[]> downloadDispensableGene(HttpServletRequest request,
                                                          @PathVariable String filename) throws IOException {
        Path dispensablePath = pathUtils.getAbsoluteDispensableGenePath().resolve(filename);
        if (!Files.exists(dispensablePath)) {
            throw new NotFoundException();
        }
        String mimeType = URLConnection
            .guessContentTypeFromName(dispensablePath.getFileName().toString());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDispositionFormData("attachment",
            dispensablePath.getFileName().toString());
        logger.info("download file! ip={}, filePath={}", getClientIP(request), dispensablePath);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(dispensablePath.toFile()),
            headers, HttpStatus.CREATED);
    }
}
