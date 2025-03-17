package com.bentego.cdputils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;

@Service
public class SpringLogService {
    private static final Logger logger = LoggerFactory.getLogger(SpringLogService.class);
    private static final String LOG_FILE_PATH = "healthcheck.log";

    public void logToFile(String functionName, String data) {
        String logEntry = String.format("[%s] %s%n", functionName, data);

        try {
            Files.write(Paths.get(LOG_FILE_PATH), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            logger.info("Log kaydedildi: {}", functionName);
        } catch (IOException e) {
            logger.error("Log dosyasına yazarken hata oluştu", e);
        }
    }
}