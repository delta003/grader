/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

public interface VolumeFactory {

    interface VolumeRequest {
        String name();
    }

    interface Volume {
        String name();
        void remove();
    }

    Volume create(VolumeRequest request);
}
