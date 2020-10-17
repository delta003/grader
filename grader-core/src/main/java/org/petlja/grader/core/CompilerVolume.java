/*
 * (c) Copyright 2020 Petlja. All rights reserved.
 */

package org.petlja.grader.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.petlja.grader.docker.CreateVolumeRequest;
import org.petlja.grader.docker.Volume;
import org.petlja.grader.docker.VolumeId;
import org.petlja.grader.docker.VolumeManager;

public final class CompilerVolume implements Supplier<Volume> {

    private static final VolumeId COMPILER_VOLUME = VolumeId.of("compiler");

    private final Volume volume;

    private static CompilerVolume instance = null;

    public static CompilerVolume create(VolumeManager volumeManager) {
        if (instance == null) {
            instance = new CompilerVolume(volumeManager);
        }
        return instance;
    }

    private CompilerVolume(VolumeManager volumeManager) {
        this.volume = volumeManager.create(CreateVolumeRequest.builder().volume(COMPILER_VOLUME).build()).getVolume();
        File compileScriptSrc;
        try {
            compileScriptSrc = new File(CompilerVolume.class.getResource("/cpp/compile.sh").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File compileScriptDest = new File(volume.getMountpoint() + "/cpp.sh");
        try {
            FileUtils.moveFile(compileScriptSrc, compileScriptDest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Volume get() {
        return volume;
    }

}
