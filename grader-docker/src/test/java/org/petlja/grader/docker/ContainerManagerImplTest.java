/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class ContainerManagerImplTest {
    // docker/getting-started
    private static final ImageId IMAGE_ID = ImageId.of("1f32459ef038");

    private DockerClient dockerClient;
    private ContainerManager containerManager;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before() {
        this.dockerClient = DockerClientFactory.create();
        this.containerManager = new ContainerManagerImpl(dockerClient);
    }

    @Test
    public void testLifecycle() {
        LaunchContainerResponse containerResponse =
                containerManager.launch(LaunchContainerRequest.builder().image(IMAGE_ID).build());
        assertThat(dockerClient.listContainersCmd()
                .withIdFilter(ImmutableSet.of(containerResponse.getContainer().getId().get())).exec())
                .isNotEmpty();
        containerManager.destroy(DestroyContainerRequest.builder()
                .container(containerResponse.getContainer().getId())
                .build());
        assertThat(dockerClient.listContainersCmd()
                .withIdFilter(ImmutableSet.of(containerResponse.getContainer().getId().get())).exec())
                .isEmpty();
    }

    @Test
    public void testBinds() throws IOException {
        LaunchContainerResponse containerResponse =
                containerManager.launch(LaunchContainerRequest.builder()
                        .image(IMAGE_ID)
                        .binds(BindRequest.builder()
                                .path(folder.newFolder().toPath().toString())
                                .destination("/app/input")
                                .ro(true)
                                .build())
                        .binds(BindRequest.builder()
                                .path(folder.newFolder().toPath().toString())
                                .destination("/app/output")
                                .ro(false)
                                .build())
                        .build());

        InspectContainerResponse inspectContainerResponse =
                dockerClient.inspectContainerCmd(containerResponse.getContainer().getId().get()).exec();
        assertThat(inspectContainerResponse.getMounts().size()).isEqualTo(2);
        assertThat(inspectContainerResponse.getMounts().stream()
                .anyMatch(mount -> mount.getDestination().getPath().equals("/app/input") && !mount.getRW()))
                .isTrue();
        assertThat(inspectContainerResponse.getMounts().stream()
                .anyMatch(mount -> mount.getDestination().getPath().equals("/app/output") && mount.getRW()))
                .isTrue();

        containerManager.destroy(DestroyContainerRequest.builder()
                .container(containerResponse.getContainer().getId()).build());
    }
}
