package com.fangdd.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lantian
 * @date 2019/09/03
 */
@Configuration
public class PidFileConfig implements EnvironmentAware {

    private Environment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(PidFileConfig.class);

    @EventListener(value = ApplicationReadyEvent.class)
    public void write() throws IOException {
        String pid = null;
        Path pidFilePath = null;
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            // get pid
            pid = name.split("@")[0];

            pidFilePath = getPidFilePath();

            List<String> data = new ArrayList<>(1);
            data.add(pid);
            Files.write(pidFilePath, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            LOGGER.info("write pid to file, pid: {}, file: {}", pid, pidFilePath);
        } catch (IOException e) {
            LOGGER.error("write pid to file fail, pid: {}, file: {}", pid, pidFilePath);
            throw e;
        }
    }

    @EventListener(value = ContextClosedEvent.class)
    public void delete() throws IOException {
        Path pidFilePath = getPidFilePath();
        try {
            Files.delete(pidFilePath);
        } catch (NoSuchFileException e) {
            ;
        }
        LOGGER.info("delete pid file, file: {}", pidFilePath);
    }

    public Path getPidFilePath() {
        String dataDir = System.getenv("DATA_DIR");
        if (StringUtils.isEmpty(dataDir)) {
            dataDir = environment.getProperty("DATA_DIR");
        }
        File file = new File(dataDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return Paths.get(dataDir, "pid.txt");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
