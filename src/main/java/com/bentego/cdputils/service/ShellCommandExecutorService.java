package com.bentego.cdputils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ShellCommandExecutorService {

    Logger logger = LoggerFactory.getLogger(ShellCommandExecutorService.class);

    public ShellCommandExecutorService() {

    }

    public String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            logger.info("shell command to run: {}", command);
            processBuilder.command("bash", "-c", command);

            Process process = processBuilder.start();

            // Komutun çıktısını oku
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Hata çıktısını kontrol et
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    logger.error("command error: {}", errorLine);
                }
            }

            process.waitFor(); // İşlem tamamlanmasını bekle

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing command: " + e.getMessage();
        }

        return output.toString();
    }

}
