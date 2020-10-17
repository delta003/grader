/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

public interface VolumeManager {

    CreateVolumeResponse create(CreateVolumeRequest request);

    void destroy(DestroyVolumeRequest request);
}
