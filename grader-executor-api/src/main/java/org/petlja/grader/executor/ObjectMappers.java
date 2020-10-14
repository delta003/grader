/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public final class ObjectMappers {

    public static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    private ObjectMappers() { }
}
