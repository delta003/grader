/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;

public final class ObjectMappersTest {

    @Test
    public void testDeserialization() throws URISyntaxException, IOException {
        Configuration read = ObjectMappers.YAML_OBJECT_MAPPER.readValue(
                new File(ObjectMappersTest.class.getResource("/configuration.yml").toURI()),
                Configuration.class);
        assertThat(read.tasks()).containsExactly(
                ImmutableCommand.of(ImmutableList.of("time", "sleep", "10")),
                ImmutableCommand.of(ImmutableList.of("times")),
                ImmutableCommand.of(ImmutableList.of("ls", "-la")));
    }
}
