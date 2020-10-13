/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableConfiguration.class)
@JsonDeserialize(as = ImmutableConfiguration.class)
public interface Configuration {

    String CONFIGURATION_FILE_PATH = "/app/executor/configuration.yml";

    @Value.Immutable
    @JsonSerialize(as = ImmutableCommand.class)
    @JsonDeserialize(as = ImmutableCommand.class)
    interface Command {
        @Value.Parameter
        List<String> commands();
    }

    @Value.Parameter
    List<Command> tasks();
}
