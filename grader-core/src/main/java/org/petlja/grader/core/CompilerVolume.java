/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.petlja.grader.docker.BindRequest;

public final class CompilerVolume implements Supplier<BindRequest> {

    private final BindRequest bindRequest;

    private static CompilerVolume instance = null;

    public static CompilerVolume create() {
        if (instance == null) {
            instance = new CompilerVolume();
        }
        return instance;
    }

    private CompilerVolume() {
        try {
            File compileScriptSrc = new File(CompilerVolume.class.getResource("/cpp/compile.sh").getPath());
            File compileScriptDest = Paths.get(Constants.COMPILER_DIR, "cpp.sh").toFile();
            FileUtils.moveFile(compileScriptSrc, compileScriptDest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bindRequest = BindRequest.builder()
                .path(Constants.COMPILER_DIR)
                .destination(Constants.EXECUTOR_COMPILER_DIR)
                .ro(true)
                .build();
    }

    @Override
    public BindRequest get() {
        return bindRequest;
    }

}
