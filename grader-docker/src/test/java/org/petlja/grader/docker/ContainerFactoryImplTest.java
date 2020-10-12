/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.ConflictException;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public final class ContainerFactoryImplTest {
    // docker/getting-started
    private static final String IMAGE_ID = "1f32459ef038";

    private DockerClient dockerClient;
    private ContainerFactory containerFactory;

    @Before
    public void before() {
        this.dockerClient = DockerClientFactory.create();
        this.containerFactory = new ContainerFactoryImpl(dockerClient);
    }

    @Test
    public void testLifecycle() {
        ContainerFactory.Container container = containerFactory.create(new ContainerFactory.ContainerRequest() {
            @Override
            public String image() {
                return IMAGE_ID;
            }

            @Override
            public Optional<VolumeFactory.Volume> volume() {
                return Optional.empty();
            }
        });
        container.stop();
        container.remove();
        assertThat(dockerClient.listContainersCmd().withIdFilter(ImmutableSet.of(container.container())).exec())
                .isEmpty();
    }

    @Test
    public void testVolume() {
        VolumeFactory.Volume volume = new VolumeFactoryImpl(dockerClient).create(new VolumeFactory.VolumeRequest() {
            @Override
            public String name() {
                return "test-volume";
            }
        });
        ContainerFactory.Container container = containerFactory.create(new ContainerFactory.ContainerRequest() {
            @Override
            public String image() {
                return IMAGE_ID;
            }

            @Override
            public Optional<VolumeFactory.Volume> volume() {
                return Optional.of(volume);
            }
        });

        assertThatExceptionOfType(ConflictException.class)
                .isThrownBy(volume::remove)
                .withMessageContaining("volume is in use");

        container.stop();
        container.remove();

        volume.remove();
    }
}
