/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Key;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** First-wins fallback for the installed cable texture omitted by the blocks atlas. */
final class InstalledCableTexture {

    static final Key KEY = Key.parse("integrateddynamics:block/cable");
    static final String RESOURCE =
            "assets/integrateddynamics/textures/block/cable.png";

    private static final long MAX_BYTES = 1024L * 1024L;
    private static final int EXPECTED_SIZE = 16;
    private static final int MAX_ROOTS = 4_096;

    private InstalledCableTexture() {
    }

    static Texture find(ResourcePack resourcePack, Iterable<Path> roots)
            throws IOException, InterruptedException {
        Capture capture = new Capture();
        int rootCount = 0;
        for (Path root : roots) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            if (++rootCount > MAX_ROOTS) {
                return null;
            }
            resourcePack.loadResourcePath(root, activeRoot ->
                    capture.claim(activeRoot.resolve(RESOURCE)));
            if (capture.claimed) {
                return capture.texture;
            }
        }
        return null;
    }

    static boolean installIfMissing(ResourcePack resourcePack, Texture fallback) {
        if (resourcePack.getTextures().get(KEY) == null && fallback != null) {
            resourcePack.getTextures().putIfAbsent(KEY, fallback);
        }
        return resourcePack.getTextures().get(KEY) != null;
    }

    private static final class Capture {

        private boolean claimed;
        private Texture texture;

        private void claim(Path candidate) {
            if (claimed || !Files.isRegularFile(candidate)) {
                return;
            }
            claimed = true;
            try {
                if (Files.size(candidate) > MAX_BYTES) {
                    return;
                }
                BufferedImage image;
                try (InputStream input = Files.newInputStream(candidate)) {
                    image = ImageIO.read(input);
                }
                if (image == null || image.getWidth() != EXPECTED_SIZE
                        || image.getHeight() != EXPECTED_SIZE) {
                    return;
                }
                texture = Texture.from(KEY, image);
            } catch (IOException | RuntimeException ignored) {
                // A broken higher-priority resource must fail closed, not fall through.
            }
        }
    }
}
