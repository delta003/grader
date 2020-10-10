/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.source;

import org.petlja.grader.master.api.language.Language;

public interface SourceCode {

    String code();

    Language language();

}
