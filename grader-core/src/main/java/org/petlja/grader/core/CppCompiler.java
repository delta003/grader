/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.util.logging.Logger;
import org.petlja.grader.docker.Container;
import org.petlja.grader.docker.ContainerManager;
import org.petlja.grader.docker.DestroyContainerRequest;
import org.petlja.grader.docker.DestroyVolumeRequest;
import org.petlja.grader.docker.LaunchContainerRequest;
import org.petlja.grader.docker.MountVolumeRequest;
import org.petlja.grader.docker.VolumeManager;
import org.petlja.grader.executor.ExecutorConfiguration;
import org.petlja.grader.executor.ExecutorProcess;
import org.petlja.grader.executor.ProcessCommand;

public final class CppCompiler implements Compiler {

    private static final Logger logger = Logger.getLogger(CppCompiler.class.getName());

    private static final ExecutorConfiguration EXECUTOR_CONFIGURATION = ExecutorConfiguration.builder()
            .processes(ExecutorProcess.builder()
                    .commands(ProcessCommand.of("bash"))
                    .commands(ProcessCommand.of("/app/compiler/cpp.sh"))
                    .commands(ProcessCommand.of("/app/executor/source"))
                    .commands(ProcessCommand.of("/app/executor/output"))
                    .commands(ProcessCommand.of("/app/executor/error"))
                    .commands(ProcessCommand.of("/app/executor/time"))
                    .build())
            .build();

    private final VolumeManager volumeManager;
    private final ContainerManager containerManager;

    public CppCompiler(VolumeManager volumeManager, ContainerManager containerManager) {
        this.volumeManager = volumeManager;
        this.containerManager = containerManager;
    }

    @Override
    public CompileResponse compile(CompileRequest request) {
        CompilerVolume compilerVolume = CompilerVolume.create(volumeManager);
        UserCodeVolume userCodeVolume = new UserCodeVolume(
                volumeManager, request.getId(), request.getSource(), EXECUTOR_CONFIGURATION);
        Container executor = containerManager.launch(
                LaunchContainerRequest.builder()
                        .image(EXECUTOR_IMAGE_ID)
                        .volumes(MountVolumeRequest.builder()
                                .volume(compilerVolume.get().getId())
                                .destination("/app/compiler")
                                .ro(true)
                                .build())
                        .volumes(MountVolumeRequest.builder()
                                .volume(userCodeVolume.get().getId())
                                .destination("/app/executor")
                                .ro(false)
                                .build())
                        .build()).getContainer();
        CompileResponse response = parseExecutor(userCodeVolume);
        containerManager.destroy(DestroyContainerRequest.builder().container(executor.getId()).build());
        volumeManager.destroy(DestroyVolumeRequest.builder().volume(userCodeVolume.get().getId()).build());
        return response;
    }

    private CompileResponse parseExecutor(UserCodeVolume userCodeVolume) {
        // TODO(mbakovic): get output error and time
        return null;
    }
}
