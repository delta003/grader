/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class ExecutorProcessRunnerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File logFile;
    private ExecutorProcessRunner executorProcessRunner;

    @Before
    public void before() throws IOException {
        logFile = folder.newFile();
        executorProcessRunner = new ExecutorProcessRunner(logFile);
    }

    @Test
    public void testRun() throws IOException, InterruptedException {
        executorProcessRunner.execute(ExecutorProcess.builder()
                .commands(ProcessCommand.of("gtime"))
                .commands(ProcessCommand.of("ls"))
                .commands(ProcessCommand.of("-la"))
                .build());
        String log = Files.readString(logFile.toPath());
        assertThat(log).contains("user");
        assertThat(log).contains("system");
        assertThat(log).contains("elapsed");
    }
}
