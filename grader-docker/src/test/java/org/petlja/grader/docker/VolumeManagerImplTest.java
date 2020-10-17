/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.DockerClient;
import org.junit.Before;
import org.junit.Test;

public final class VolumeManagerImplTest {
    private static final VolumeId VOLUME_ID = VolumeId.of("test-volume");

    private DockerClient dockerClient;

    @Before
    public void before() {
        this.dockerClient = DockerClientFactory.create();
    }

    @Test
    public void testLifecycle() {
        VolumeManager volumeManager = new VolumeManagerImpl(dockerClient);
        CreateVolumeResponse volumeResponse =
                volumeManager.create(CreateVolumeRequest.builder().volume(VOLUME_ID).build());
        assertThat(dockerClient.listVolumesCmd().exec().getVolumes().stream()
                .anyMatch(inspectVolumeResponse -> inspectVolumeResponse.getName().equals(VOLUME_ID.get())))
                .isTrue();
        volumeManager.destroy(DestroyVolumeRequest.builder().volume(volumeResponse.getVolume().getId()).build());
    }
}
