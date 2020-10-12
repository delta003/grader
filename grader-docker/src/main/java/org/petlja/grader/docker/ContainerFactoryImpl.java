/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;

public final class ContainerFactoryImpl implements ContainerFactory {

    private final DockerClient dockerClient;

    public ContainerFactoryImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public Container create(ContainerRequest request) {
        CreateContainerCmd createContainerCmd = this.dockerClient.createContainerCmd(request.image());
        request.volume().ifPresent(volume -> {
            Volume dockerVolume = new Volume("/app/test-volume");
            createContainerCmd.withVolumes(dockerVolume);
            createContainerCmd.withHostConfig(
                    HostConfig.newHostConfig()
                            .withBinds(new Bind(volume.name(), dockerVolume, AccessMode.ro)));
        });
        CreateContainerResponse createContainerResponse = createContainerCmd.exec();
        String container = createContainerResponse.getId();
        this.dockerClient.startContainerCmd(container).exec();
        return new Container() {
            @Override
            public String container() {
                return container;
            }

            @Override
            public void stop() {
                dockerClient.stopContainerCmd(container).exec();
            }

            @Override
            public void remove() {
                dockerClient.removeContainerCmd(container).exec();
            }
        };
    }
}
