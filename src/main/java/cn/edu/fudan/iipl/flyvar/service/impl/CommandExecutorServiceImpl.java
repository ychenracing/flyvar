package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

    // private static final String ERROR_OUTPUT_PATTERN = "\\s2\\s?>\\s?\\S+";

    // private static final String OUTPUT_PATTERN = "\\s1?\\s?>\\s?(\\S+)";

    // private static final Pattern outputPattern = Pattern.compile(OUTPUT_PATTERN);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void execute(String command) {
        if (StringUtils.isBlank(command)) {
            logger.error("Command is blank, will not be executed!, cmd=" + command);
            return;
        }
        List<String> commands = Lists.newArrayList();
        try {
            if (!isWindows()) {
                commands.addAll(Arrays.asList("/bin/bash", "-cl")); // use bash on non-windows
            } else {
                commands.addAll(Arrays.asList("cmd", "/c")); // use cmd on windows machine
            }

            // Path outputFilepath = null;
            // if (command.contains(">")) {
            // command = command.replaceAll(ERROR_OUTPUT_PATTERN, "");
            // Matcher outputMatcher = outputPattern.matcher(command);
            // if (outputMatcher.find()) {
            // outputFilepath = Paths.get(outputMatcher.group(1));
            // }
            // command = command.replaceAll(OUTPUT_PATTERN, "");
            // }
            // Collections.addAll(commands, command.split("\\s+"));
            //
            // ProcessBuilder processBuilder = new ProcessBuilder(commands);
            // if (outputFilepath != null) {
            // processBuilder.redirectOutput(outputFilepath.toFile());
            // }
            // Process process = processBuilder.start();
            //
            // if (outputFilepath == null) {
            // taskExecutor.execute(removeInfoInStream(process.getInputStream()));
            // }

            commands.add(command);
            Process process = Runtime.getRuntime().exec(commands.toArray(new String[0]));
            taskExecutor.execute(removeInfoInStream(process.getInputStream()));
            taskExecutor.execute(removeInfoInStream(process.getErrorStream()));

            if (process.waitFor() != 0) {
                logger.error("Command executed failed!, cmd=" + command);
            }
        } catch (Exception ex) {
            logger.error("Command executed failed!, cmd=" + command, ex);
        }
        logger.info("command execute finished! command={}", command);
    }

    private Runnable removeInfoInStream(InputStream is) {
        return () -> {
            try (InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr)) {
                // remove information, to prevent output info blocking the pipe, which may lead to
                // pause the command executing.
                while (br.readLine() != null) {
                }
            } catch (Exception e) {
                logger.error("error reading stream information when command is executing!", e);
            }
        };
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
