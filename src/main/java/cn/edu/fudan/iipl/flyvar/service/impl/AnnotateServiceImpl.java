package cn.edu.fudan.iipl.flyvar.service.impl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CommandExecutorService;

/**
 * @author racing
 * @version $Id: AnnovarUtils.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
@Service
public class AnnotateServiceImpl implements AnnotateService {

    @Autowired
    private CommandExecutorService commandExecutorService;

    @Autowired
    private AnnovarUtils           annovarUtils;

    @Override
    public Path annotateVariationVcfFormat(Path variationPath) {
        commandExecutorService.executeSerially(
            annovarUtils.getAnnovarCommands(variationPath.getFileName().toString()));
        return variationPath;
    }

}
