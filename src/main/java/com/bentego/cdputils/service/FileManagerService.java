package com.bentego.cdputils.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileManagerService {

    Logger logger = LoggerFactory.getLogger(FileManagerService.class);

    public void mkdirIfNotExistsAs755(String dirPath) throws IOException {

        logger.info("creating directory on path with permission with 755: {}", dirPath);
        Path path = Paths.get(dirPath);

        if (Files.exists(path)) {
            logger.info("path already exist: {} no need to create", dirPath);
            return;
        }


        Files.createDirectories(path);
        logger.info("directory created on path: {}", dirPath);

        Set<PosixFilePermission> permissions = new HashSet<>(Arrays.asList(
                PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE
        ));

        Files.setPosixFilePermissions(path, permissions);
        logger.info("permissions set as 755 for directory: {}", dirPath);
    }
}