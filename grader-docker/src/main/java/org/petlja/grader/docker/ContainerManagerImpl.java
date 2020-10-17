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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class ContainerManagerImpl implements ContainerManager {

    private static final Logger logger = Logger.getLogger(ContainerManagerImpl.class.getName());

    private final DockerClient dockerClient;

    public ContainerManagerImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public LaunchContainerResponse launch(LaunchContainerRequest request) {
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(request.getImage().get());
        if (!request.getVolumes().isEmpty()) {
            List<Bind> binds = new ArrayList<>();
            createContainerCmd.withVolumes(
                    request.getVolumes().stream()
                            .map(volume -> {
                                Volume dockerVolume = new Volume(volume.getDestination());
                                binds.add(new Bind(
                                        volume.getVolume().get(),
                                        dockerVolume,
                                        volume.getRo() ? AccessMode.ro : AccessMode.rw));
                                return dockerVolume;
                            })
                            .collect(Collectors.toList()));
            createContainerCmd.withHostConfig(HostConfig.newHostConfig().withBinds(binds));
        }
        CreateContainerResponse createContainerResponse = createContainerCmd.exec();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        logger.info(String.format("Started container %s",
                createContainerResponse.getId()));
        return LaunchContainerResponse.builder()
                .container(Container.builder()
                        .id(ContainerId.of(createContainerResponse.getId()))
                        .build())
                .build();
    }

    @Override
    public void destroy(DestroyContainerRequest request) {
        dockerClient.stopContainerCmd(request.getContainer().get()).exec();
        dockerClient.removeContainerCmd(request.getContainer().get()).exec();
        logger.info(String.format("Destroyed container %s",
                request.getContainer().get()));
    }
}
