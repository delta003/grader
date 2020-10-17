/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import com.github.dockerjava.api.DockerClient;
import java.util.logging.Logger;

public final class VolumeManagerImpl implements VolumeManager {

    private static final Logger logger = Logger.getLogger(VolumeManagerImpl.class.getName());

    private final DockerClient dockerClient;

    public VolumeManagerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public CreateVolumeResponse create(CreateVolumeRequest request) {
        com.github.dockerjava.api.command.CreateVolumeResponse
                createVolumeResponse = dockerClient.createVolumeCmd().withName(request.getVolume().get()).exec();
        logger.info(String.format("Created volume %s at mountpoint %s",
                createVolumeResponse.getName(),
                createVolumeResponse.getMountpoint()));
        return CreateVolumeResponse.builder()
                .volume(Volume.builder()
                        .id(VolumeId.of(createVolumeResponse.getName()))
                        .mountpoint(createVolumeResponse.getMountpoint())
                        .build())
                .build();
    }

    @Override
    public void destroy(DestroyVolumeRequest request) {
        dockerClient.removeVolumeCmd(request.getVolume().get()).exec();
        logger.info(String.format("Destroyed volume %s", request.getVolume().get()));
    }
}
