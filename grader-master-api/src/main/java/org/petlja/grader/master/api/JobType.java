/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api;

/**
 * Each job type runs in different executor. Type is passed to scheduler to retrieve executor.
 */
public enum JobType {

    COMPETITION_PROBLEM,

}
