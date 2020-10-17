/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.docker;

public interface ContainerManager {

    LaunchContainerResponse launch(LaunchContainerRequest request);

    void destroy(DestroyContainerRequest request);
}
