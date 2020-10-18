/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

public final class Constants {

    public static final String WORK_DIR = "/Volumes/git/grader/workdir";
    public static final String COMPILER_DIR = WORK_DIR + "/compiler";

    // TODO(mbakovic): Executor dir is hardcoded in executor contants
    public static final String EXECUTOR_DIR = "/app/executor";
    public static final String EXECUTOR_COMPILER_DIR = "/app/compiler";

    private Constants() {}
}
