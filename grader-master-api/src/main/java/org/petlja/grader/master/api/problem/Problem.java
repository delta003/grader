/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.problem;

import org.petlja.grader.master.api.solution.Solution;

/**
 * Properties for executable job type.
 */
public interface Problem {

    ProblemType type();

    Limits limits();

    Solution solution();



}
