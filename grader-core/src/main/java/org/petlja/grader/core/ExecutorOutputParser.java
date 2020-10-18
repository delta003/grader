/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.nio.file.Path;

public interface ExecutorOutputParser {
    CompileResponse parse(Path userCodeWorkDir);
}
