/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.util.logging.Logger;
import org.petlja.grader.docker.Container;
import org.petlja.grader.docker.ContainerManager;
import org.petlja.grader.docker.DestroyContainerRequest;
import org.petlja.grader.docker.LaunchContainerRequest;
import org.petlja.grader.executor.ExecutorConfiguration;
import org.petlja.grader.executor.ExecutorProcess;
import org.petlja.grader.executor.ProcessCommand;

public final class CppCompiler implements Compiler {

    private static final Logger logger = Logger.getLogger(CppCompiler.class.getName());

    private static final ExecutorConfiguration EXECUTOR_CONFIGURATION = ExecutorConfiguration.builder()
            .processes(ExecutorProcess.builder()
                    .commands(ProcessCommand.of("bash"))
                    .commands(ProcessCommand.of(Constants.EXECUTOR_COMPILER_DIR + "/cpp.sh"))
                    .commands(ProcessCommand.of(Constants.EXECUTOR_DIR + "/source"))
                    .commands(ProcessCommand.of(Constants.EXECUTOR_DIR + "/output"))
                    .commands(ProcessCommand.of(Constants.EXECUTOR_DIR + "/error"))
                    .commands(ProcessCommand.of(Constants.EXECUTOR_DIR + "/time"))
                    .build())
            .build();

    private final ContainerManager containerManager;
    private final ExecutorOutputParser executorOutputParser;

    public CppCompiler(ContainerManager containerManager, ExecutorOutputParser executorOutputParser) {
        this.containerManager = containerManager;
        this.executorOutputParser = executorOutputParser;
    }

    @Override
    public CompileResponse compile(CompileRequest request) {
        CompilerVolume compilerVolume = CompilerVolume.create();
        UserCodeVolume userCodeVolume = new UserCodeVolume(
                request.getId(), request.getSource(), EXECUTOR_CONFIGURATION);
        Container executor = containerManager.launch(
                LaunchContainerRequest.builder()
                        .image(EXECUTOR_IMAGE_ID)
                        .binds(compilerVolume.get())
                        .binds(userCodeVolume.get())
                        .build()).getContainer();
        containerManager.destroy(DestroyContainerRequest.builder().container(executor.getId()).build());
        return executorOutputParser.parse(userCodeVolume.userCodeDirectory());
    }
}
