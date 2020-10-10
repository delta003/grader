/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class CppCompiler implements Compiler {

    private static final Logger logger = Logger.getLogger(CppCompiler.class.getName());

    private final File compileScript;
    private final File compilerLog;

    public CppCompiler() throws URISyntaxException {
        this.compileScript = new File(CppCompiler.class.getResource("/cpp/compile.sh").toURI());
        this.compilerLog = new File(CppCompiler.class.getResource("/cpp/compiler-log.txt").toURI());
    }

    @Override
    public CompileResponse compile(CompileRequest request) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "bash",
                        this.compileScript.toPath().toString(),
                        request.source().toString(),
                        request.out().toString(),
                        request.error().toString())
                .redirectOutput(ProcessBuilder.Redirect.appendTo(this.compilerLog))
                .redirectError(ProcessBuilder.Redirect.appendTo(this.compilerLog));

        logger.info(String.format("Executing command: %s", processBuilder.command()));

        Process process = processBuilder.start();
        boolean completedProcess = process.waitFor(COMPILE_TIME_LIMIT.toMillis(), TimeUnit.MILLISECONDS);

        if (!completedProcess) {
            killProcess(process);
            return timeout();
        }

        logger.info(String.valueOf(process.toHandle().pid()));

        boolean succeededProcess = process.exitValue() == 0;
        // TODO(mbakovic): Fix time
        Duration time = process.info().totalCpuDuration().orElse(Duration.ZERO);
        killProcess(process);

        return succeededProcess
                ? succeeded(time)
                : failed(time);
    }

    private void killProcess(Process process) {
        process.destroy();
        if (process.isAlive()) {
            process.destroyForcibly();
        }
    }
}
