/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

public interface Compiler {

    Duration COMPILE_TIME_LIMIT = Duration.ofSeconds(60);

    interface CompileRequest {
        Path source();
        Path out();
        Path error();
    }

    enum CompileStatus {
        SUCCEEDED,
        FAILED,
        TIMEOUT
    }

    interface CompileResponse {
        CompileStatus status();
        Duration time();
    }

    default CompileResponse timeout() {
        return new CompileResponse() {
            @Override
            public CompileStatus status() {
                return CompileStatus.TIMEOUT;
            }

            @Override
            public Duration time() {
                return COMPILE_TIME_LIMIT;
            }
        };
    }

    default CompileResponse succeeded(Duration time) {
        return new CompileResponse() {
            @Override
            public CompileStatus status() {
                return CompileStatus.SUCCEEDED;
            }

            @Override
            public Duration time() {
                return time;
            }
        };
    }

    default CompileResponse failed(Duration time) {
        return new CompileResponse() {
            @Override
            public CompileStatus status() {
                return CompileStatus.FAILED;
            }

            @Override
            public Duration time() {
                return time;
            }
        };
    }

    CompileResponse compile(CompileRequest request) throws InterruptedException, IOException;
}
