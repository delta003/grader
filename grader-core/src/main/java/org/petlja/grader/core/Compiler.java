/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.time.Duration;
import org.petlja.grader.docker.ImageId;

public interface Compiler {

    ImageId EXECUTOR_IMAGE_ID = ImageId.of("dd3a5972c962");

    // TODO(mbakovic): Support timeout
    Duration COMPILE_TIME_LIMIT = Duration.ofSeconds(60);

    CompileResponse compile(CompileRequest request);
}
