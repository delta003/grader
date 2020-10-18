/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;
import org.petlja.grader.docker.BindRequest;
import org.petlja.grader.executor.ExecutorConfiguration;
import org.petlja.grader.executor.ObjectMappers;

public final class UserCodeVolume implements Supplier<BindRequest> {

    private static final String DIRECTORY_TEMPLATE = "/user-code-%s";

    private final BindRequest bindRequest;
    private final Path userCodeWorkDir;

    public UserCodeVolume(UUID id, String source, ExecutorConfiguration configuration) {
        userCodeWorkDir = Paths.get(Constants.WORK_DIR, String.format(DIRECTORY_TEMPLATE, id));
        try {
            boolean created = userCodeWorkDir.resolve("source").toFile().createNewFile();
            if (!created) {
                throw new RuntimeException("Source file already exist");
            }
            Files.writeString(userCodeWorkDir.resolve("source"), source);
            ObjectMappers.YAML_OBJECT_MAPPER.writeValue(
                    userCodeWorkDir.resolve("configuration.yml").toFile(), configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bindRequest = BindRequest.builder()
                .path(userCodeWorkDir.toAbsolutePath().toString())
                .destination(Constants.EXECUTOR_DIR)
                .ro(false)
                .build();
    }

    @Override
    public BindRequest get() {
        return bindRequest;
    }

    public Path userCodeDirectory() {
        return userCodeWorkDir;
    }
}
