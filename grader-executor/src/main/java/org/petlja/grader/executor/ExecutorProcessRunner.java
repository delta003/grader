/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class ExecutorProcessRunner {

    private static final Logger logger = Logger.getLogger(ExecutorProcessRunner.class.getName());

    private static final Duration PROCESS_TIME_LIMIT = Duration.ofSeconds(60);

    private final File executorOutputPath;

    public ExecutorProcessRunner(File executorOutputPath) {
        this.executorOutputPath = executorOutputPath;
    }

    public void execute(ExecutorProcess process) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                process.getCommands().stream().map(ProcessCommand::get).collect(Collectors.toList()))
                .redirectOutput(ProcessBuilder.Redirect.appendTo(executorOutputPath))
                .redirectError(ProcessBuilder.Redirect.appendTo(executorOutputPath));

        logger.info(String.format("Executing command: %s", processBuilder.command()));

        Process externalProcess = processBuilder.start();

        Instant startedInstant = Instant.now();
        boolean completedProcess = externalProcess.waitFor(PROCESS_TIME_LIMIT.toMillis(), TimeUnit.MILLISECONDS);
        Instant completedInstant = Instant.now();

        logger.info(String.format("Duration %s", Duration.between(startedInstant, completedInstant)));

        if (!completedProcess) {
            killProcess(externalProcess);
        }
    }

    private void killProcess(Process process) {
        process.destroy();
        if (process.isAlive()) {
            process.destroyForcibly();
        }
    }
}
