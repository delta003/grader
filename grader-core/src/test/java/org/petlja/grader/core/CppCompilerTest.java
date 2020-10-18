/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import com.github.dockerjava.api.DockerClient;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.petlja.grader.docker.ContainerManagerImpl;
import org.petlja.grader.docker.DockerClientFactory;

public final class CppCompilerTest {

    @Mock
    private ExecutorOutputParser executorOutputParser;

    private File code;
    private File codeCompileFail;
    private Compiler compiler;

    @Before
    public void before() throws URISyntaxException {
        this.code = new File(CppCompilerTest.class.getResource("/cpp/code.cpp").toURI());
        this.codeCompileFail = new File(CppCompilerTest.class.getResource("/cpp/code-compile-fail.cpp").toURI());

        DockerClient dockerClient = DockerClientFactory.create();
        // TODO(mbakovic): Intiialize and use mock
        this.compiler = new CppCompiler(new ContainerManagerImpl(dockerClient), new ExecutorOutputParserImpl());
    }

    @Test
    public void testCompileSucceed() throws IOException {
        String source = Files.asCharSource(code, Charsets.UTF_8).read();
        CompileResponse response =
                compiler.compile(CompileRequest.builder().id(UUID.randomUUID()).source(source).build());
    }

    @Ignore("Not implemented")
    @Test
    public void testCompileFailed() {
        // TODO(mbakovic): This
    }

    @Ignore("Not implemented")
    @Test
    public void testCompileTimeout() {
        // TODO(mbakovic): This
    }

    @Ignore("Not implemented")
    @Test
    public void testCompileMemory() {
        // TODO(mbakovic): This
    }
}
