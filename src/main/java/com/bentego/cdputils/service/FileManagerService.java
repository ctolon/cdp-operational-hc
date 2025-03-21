package com.bentego.cdputils.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileManagerService {

    public void mkdirIfNotExistsAs755(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);

        if (Files.exists(path)) {
            return;
        }

        Files.createDirectories(path);

        Set<PosixFilePermission> permissions = new HashSet<>(Arrays.asList(
                PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_EXECUTE
        ));

        Files.setPosixFilePermissions(path, permissions);
    }
}