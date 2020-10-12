/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import java.util.logging.Logger;

public final class VolumeFactoryImpl implements VolumeFactory {

    private static final Logger logger = Logger.getLogger(VolumeFactoryImpl.class.getName());

    private final DockerClient dockerClient;

    public VolumeFactoryImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public Volume create(VolumeRequest request) {
        CreateVolumeResponse createVolumeResponse = this.dockerClient.createVolumeCmd().withName(request.name()).exec();

        logger.info(String.format("Created volume %s at mountpoint %s with labels %s",
                createVolumeResponse.getName(),
                createVolumeResponse.getMountpoint(),
                createVolumeResponse.getLabels()));

        return new Volume() {

            @Override
            public String name() {
                return createVolumeResponse.getName();
            }

            @Override
            public void remove() {
                dockerClient.removeVolumeCmd(createVolumeResponse.getName()).exec();
            }
        };
    }
}
