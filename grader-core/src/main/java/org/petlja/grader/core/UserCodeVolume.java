/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Supplier;
import org.petlja.grader.docker.CreateVolumeRequest;
import org.petlja.grader.docker.Volume;
import org.petlja.grader.docker.VolumeId;
import org.petlja.grader.docker.VolumeManager;
import org.petlja.grader.executor.ExecutorConfiguration;
import org.petlja.grader.executor.ObjectMappers;

public final class UserCodeVolume implements Supplier<Volume> {

    private static final String VOLUME_TEMPLATE = "user-code-%s";

    private final Volume volume;

    public UserCodeVolume(VolumeManager volumeManager, UUID id, String source, ExecutorConfiguration configuration) {
        this.volume = volumeManager.create(CreateVolumeRequest.builder()
                .volume(VolumeId.of(String.format(VOLUME_TEMPLATE, id)))
                .build()).getVolume();
        try {
            Files.writeString(Path.of(volume.getMountpoint(), "source"), source);
            ObjectMappers.YAML_OBJECT_MAPPER.writeValue(
                    Path.of(volume.getMountpoint(), "configuration.yml").toFile(), configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Volume get() {
        return volume;
    }
}
