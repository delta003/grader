/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api;

import java.util.Optional;
import java.util.UUID;
import org.petlja.grader.master.api.problem.Problem;

public interface Job {

    /**
     * Unique job identifier.
     */
    UUID id();

    /**
     * Job type determines executor type.
     */
    JobType type();

    /**
     * Must be preset if job type is COMPETITION_PROBLEM.
     */
    Optional<Problem> competitionProblem();

}
