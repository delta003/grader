/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.checker;

import java.io.File;
import org.petlja.grader.master.api.source.Executable;

public interface ExecutableChecker extends Executable {

    /**
     * Passed to executable as first argument.
     */
    File userOutput();

    /**
     * Passed to executable as second argument. This is expected output.
     */
    File testCaseOutput();

    /**
     * Passed to executable as third argument.
     */
    File testCaseInput();

    /**
     * Passed to executable as fourth argument. Checker should write to this file.
     */
    File checkerOutput();

}
