/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import com.github.dockerjava.api.DockerClient;
import org.junit.Before;
import org.junit.Test;

public final class VolumeFactoryImplTest {
    private static final String NAME = "test-volume";

    private DockerClient dockerClient;

    @Before
    public void before() {
        this.dockerClient = DockerClientFactory.create();
    }

    @Test
    public void testLifecycle() {
        VolumeFactory volumeFactory = new VolumeFactoryImpl(dockerClient);
        VolumeFactory.Volume volume = volumeFactory.create(() -> NAME);
        volume.remove();
    }
}
