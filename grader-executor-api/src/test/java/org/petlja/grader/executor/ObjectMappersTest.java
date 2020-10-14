/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public final class ObjectMappersTest {

    @Test
    public void testDeserialization() throws URISyntaxException, IOException {
        ExecutorConfiguration read = ObjectMappers.YAML_OBJECT_MAPPER.readValue(
                new File(ObjectMappersTest.class.getResource("/configuration.yml").toURI()),
                ExecutorConfiguration.class);
        Assertions.assertThat(read.getProcesses()).containsExactly(
                ExecutorProcess.builder()
                        .commands(ProcessCommand.of("time"))
                        .commands(ProcessCommand.of("sleep"))
                        .commands(ProcessCommand.of("10"))
                        .build(),
                ExecutorProcess.builder()
                        .commands(ProcessCommand.of("times"))
                        .build(),
                ExecutorProcess.builder()
                        .commands(ProcessCommand.of("ls"))
                        .commands(ProcessCommand.of("-la"))
                        .build());
    }
}
