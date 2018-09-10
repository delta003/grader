/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.source;

import java.io.File;

public interface Executable {

    File executable();

    File stdin();

    File stdout();

}
