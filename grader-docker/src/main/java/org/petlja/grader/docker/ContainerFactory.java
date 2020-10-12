/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

import java.util.Optional;

public interface ContainerFactory {

    interface ContainerRequest {
        String image();
        Optional<VolumeFactory.Volume> volume();
    }

    interface Container {
        String container();
        void stop();
        // Container must be stopped before removal
        void remove();
    }

    Container create(ContainerRequest request);
}
