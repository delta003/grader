/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class CppCompilerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File code;
    private File codeCompileFail;

    private Compiler compiler;

    @Before
    public void before() throws URISyntaxException {
        this.code = new File(CppCompilerTest.class.getResource("/cpp/code.cpp").toURI());
        this.codeCompileFail = new File(CppCompilerTest.class.getResource("/cpp/code-compile-fail.cpp").toURI());

        this.compiler = new CppCompiler();
    }

    @Test
    public void testCompileSucceed() throws IOException, InterruptedException {
        File out = folder.newFile();
        File error = folder.newFile();

        Compiler.CompileResponse response = compiler.compile(new Compiler.CompileRequest() {
            @Override
            public Path source() {
                return code.toPath();
            }

            @Override
            public Path out() {
                return out.toPath();
            }

            @Override
            public Path error() {
                return error.toPath();
            }
        });
        assertThat(response.status()).isEqualTo(Compiler.CompileStatus.SUCCEEDED);

        String errorContent = Files.readString(error.toPath());
        assertThat(errorContent).isEmpty();
    }

    @Test
    public void testCompileFailed() throws IOException, InterruptedException {
        File out = folder.newFile();
        File error = folder.newFile();

        Compiler.CompileResponse response = compiler.compile(new Compiler.CompileRequest() {
            @Override
            public Path source() {
                return codeCompileFail.toPath();
            }

            @Override
            public Path out() {
                return out.toPath();
            }

            @Override
            public Path error() {
                return error.toPath();
            }
        });
        assertThat(response.status()).isEqualTo(Compiler.CompileStatus.FAILED);

        String errorContent = Files.readString(error.toPath());
        assertThat(errorContent).contains("error: use of undeclared identifier 'a'");
        assertThat(errorContent).contains("error: use of undeclared identifier 'b'");
        assertThat(errorContent).contains("4 errors generated.");
    }

    @Test
    public void testCompileTime() throws IOException, InterruptedException {
        File out = folder.newFile();
        File error = folder.newFile();

        Compiler.CompileResponse response = compiler.compile(new Compiler.CompileRequest() {
            @Override
            public Path source() {
                return code.toPath();
            }

            @Override
            public Path out() {
                return out.toPath();
            }

            @Override
            public Path error() {
                return error.toPath();
            }
        });
        assertThat(response.time()).isGreaterThan(Duration.ZERO);
    }

    @Ignore("Not implemented")
    @Test
    public void testCompileRedirect() {
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
