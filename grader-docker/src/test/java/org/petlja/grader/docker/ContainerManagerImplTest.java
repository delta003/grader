/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

public final class ContainerManagerImplTest {
    // docker/getting-started
    private static final ImageId IMAGE_ID = ImageId.of("1f32459ef038");
    private static final VolumeId VOLUME_ID_1 = VolumeId.of("test-volume-1");
    private static final VolumeId VOLUME_ID_2 = VolumeId.of("test-volume-2");

    private DockerClient dockerClient;
    private ContainerManager containerManager;
    private VolumeManager volumeManager;

    @Before
    public void before() {
        this.dockerClient = DockerClientFactory.create();
        this.containerManager = new ContainerManagerImpl(dockerClient);
        this.volumeManager = new VolumeManagerImpl(dockerClient);
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
    public void testVolumes() {
        Volume volume1 = volumeManager.create(CreateVolumeRequest.builder().volume(VOLUME_ID_1).build()).getVolume();
        Volume volume2 = volumeManager.create(CreateVolumeRequest.builder().volume(VOLUME_ID_2).build()).getVolume();

        LaunchContainerResponse containerResponse =
                containerManager.launch(LaunchContainerRequest.builder()
                        .image(IMAGE_ID)
                        .volumes(MountVolumeRequest.builder()
                                .destination("/app/input")
                                .volume(volume1.getId())
                                .ro(true)
                                .build())
                        .volumes(MountVolumeRequest.builder()
                                .destination("/app/output")
                                .volume(volume2.getId())
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

        assertThatExceptionOfType(ConflictException.class)
                .isThrownBy(() -> volumeManager.destroy(DestroyVolumeRequest.builder().volume(volume1.getId()).build()))
                .withMessageContaining("volume is in use");

        containerManager.destroy(DestroyContainerRequest.builder()
                .container(containerResponse.getContainer().getId()).build());

        volumeManager.destroy(DestroyVolumeRequest.builder().volume(volume1.getId()).build());
        volumeManager.destroy(DestroyVolumeRequest.builder().volume(volume2.getId()).build());
    }
}
