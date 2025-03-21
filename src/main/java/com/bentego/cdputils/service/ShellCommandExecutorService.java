package com.bentego.cdputils.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ShellCommandExecutorService {

    public ShellCommandExecutorService() {

    }

    public String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
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
                    System.err.println("command error: " + errorLine);
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
