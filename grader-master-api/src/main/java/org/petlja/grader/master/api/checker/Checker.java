/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.checker;

import java.util.UUID;
import org.petlja.grader.master.api.source.SourceCode;

public interface Checker {

    UUID id();

    SourceCode code();

}
