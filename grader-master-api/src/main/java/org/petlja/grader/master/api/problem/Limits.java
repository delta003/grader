/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.problem;

/**
 * Various limits for COMPETITION_PROBLEM job type.
 */
public interface Limits {

    /**
     * Compile step for user code must take less than this value.
     */
    long codeCompileTimeLimitMiliseconds();

    /**
     * Each run step for user code must take less than this value.
     */
    long codeRuntimeLimitMiliseconds();

    /**
     * Compile step for checker must take less than this value.
     */
    long checkerCompileTimeLimitMiliseconds();

    /**
     * Each run step for checker must take less than this value.
     */
    long checkerRuntimeLimitMiliseconds();

    /**
     * Compilation nor runtime can take more than this value.
     */
    long memoryLimitBytes();

}
