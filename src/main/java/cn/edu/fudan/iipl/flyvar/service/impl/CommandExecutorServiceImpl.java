package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.service.CommandExecutorService;

@Service
public class CommandExecutorServiceImpl implements CommandExecutorService {

    private static final Logger    logger = LoggerFactory
        .getLogger(CommandExecutorServiceImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void execute(String command) {
        try {
            List<String> commands = Lists.newArrayList();
            if (!isWindows()) {
                commands.addAll(Arrays.asList("/bin/bash", "-cl")); // use bash on non-windows machine
            }
            commands.add(command);
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            Process process = processBuilder.start();

            try (InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr)) {
                while ((br.readLine()) != null) {
                    // remove output information, to prevent output info blocking the pipe, which may lead to pause the command executing.
                }
            }
            try (InputStream is = process.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr)) {
                while ((br.readLine()) != null) {
                    // remove err information, to prevent err info blocking the pipe, which may lead to pause the command executing.
                }
            }

            if (process.waitFor() != 0) {
                logger.error("Command executed failed!, cmd=" + command);
            }
        } catch (Exception ex) {
            logger.error("Command executed failed!, cmd=" + command, ex);
        }
    }

    @Override
    public void asyncExecute(String command) {
        taskExecutor.execute(() -> execute(command));
    }

    @Override
    public void executeSerially(List<String> commands) {
        commands.stream().forEachOrdered(command -> {
            execute(command);
        });
    }

    @Override
    public void executeConcurrently(List<String> commands) {
        commands.stream().forEach(command -> {
            asyncExecute(command);
        });
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}
