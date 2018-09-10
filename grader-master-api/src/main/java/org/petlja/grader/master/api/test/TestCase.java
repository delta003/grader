/*
 * (c) Copyright 2018 Petlja. All rights reserved.
 */

package org.petlja.grader.master.api.test;

import java.io.File;
import java.util.Optional;

public interface TestCase {

    int id();

    /**
     * Version of this test case.
     */
    int version();

    /**
     * File name for this test case.
     */
    String fileName();

    /**
     * When test case is downloaded, this is file path.
     */
    Optional<File> file();

}
