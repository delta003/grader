/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.solution;

import org.petlja.grader.master.api.source.SourceCode;

/**
 * Solution for STANDARD_IO and FUNCTIONAL problem type.
 */
public interface SourceSolution extends Solution {

    SourceCode code();

}
