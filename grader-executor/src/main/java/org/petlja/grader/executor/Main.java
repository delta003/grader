/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import java.io.File;
import java.io.IOException;

public final class Main {

    private Main() {}

    private static void validateConfiguration(ExecutorConfiguration configuration) {
        for (ExecutorProcess process : configuration.getProcesses()) {
            ExecutorErrors.throwIfProcessCommandsMustNotBeEmpty(process.getCommands().isEmpty());
            for (ProcessCommand command : process.getCommands()) {
                ExecutorErrors.throwIfCommandMustNotBeEmpty(command.get().isEmpty());
            }
        }
    }

    public static void main(String[] _args) {
        ExecutorConfiguration configuration;
        try {
            configuration = ObjectMappers.YAML_OBJECT_MAPPER.readValue(
                    new File(Constants.CONFIGURATION_PATH), ExecutorConfiguration.class);
        } catch (IOException e) {
            throw ExecutorErrors.failedToReadConfiguration(e);
        }

        validateConfiguration(configuration);

        // TODO(mbakovic): Support command timeout better
        Executor processExecutor = new Executor(new File(Constants.EXECUTOR_OUTPUT_PATH));
        for (ExecutorProcess process : configuration.getProcesses()) {
            try {
                processExecutor.execute(process);
            } catch (RuntimeException | IOException | InterruptedException e) {
                throw ExecutorErrors.processExecutionFailed(e);
            }
        }
    }
}
