/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.resources.pack.PackVersion;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstalledCableTextureTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void findsFirstDirectoryResource() throws Exception {
        Path first = temporaryDirectory.resolve("first");
        Path second = temporaryDirectory.resolve("second");
        writeDirectoryTexture(first, 0xff336699);
        writeDirectoryTexture(second, 0xffcc3300);

        Texture texture = InstalledCableTexture.find(pack(), List.of(first, second));

        assertEquals(0xff336699, texture.getTextureImage().getRGB(0, 0));
    }

    @Test
    void findsTextureInsideJarRoot() throws Exception {
        Path jar = temporaryDirectory.resolve("integrateddynamics.jar");
        try (ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(jar))) {
            output.putNextEntry(new ZipEntry(InstalledCableTexture.RESOURCE));
            output.write(png(0xff227744));
            output.closeEntry();
        }

        Texture texture = InstalledCableTexture.find(pack(), List.of(jar));

        assertEquals(0xff227744, texture.getTextureImage().getRGB(0, 0));
    }

    @Test
    void brokenHigherPriorityResourceDoesNotFallThrough() throws Exception {
        Path first = temporaryDirectory.resolve("first");
        Path broken = first.resolve(InstalledCableTexture.RESOURCE);
        Files.createDirectories(broken.getParent());
        Files.writeString(broken, "not a png");
        Path second = temporaryDirectory.resolve("second");
        writeDirectoryTexture(second, 0xff112233);

        assertNull(InstalledCableTexture.find(pack(), List.of(first, second)));
    }

    @Test
    void fallbackNeverOverwritesAtlasWinner() throws Exception {
        ResourcePack resourcePack = pack();
        Texture winner = texture(0xff123456);
        Texture fallback = texture(0xff654321);
        resourcePack.getTextures().put(InstalledCableTexture.KEY, winner);

        assertTrue(InstalledCableTexture.installIfMissing(resourcePack, fallback));
        assertSame(winner, resourcePack.getTextures().get(InstalledCableTexture.KEY));

        resourcePack.getTextures().remove(InstalledCableTexture.KEY);
        assertTrue(InstalledCableTexture.installIfMissing(resourcePack, fallback));
        assertSame(fallback, resourcePack.getTextures().get(InstalledCableTexture.KEY));

        resourcePack.getTextures().remove(InstalledCableTexture.KEY);
        assertFalse(InstalledCableTexture.installIfMissing(resourcePack, null));
    }

    private static ResourcePack pack() {
        return new ResourcePack(new PackVersion(34, 0));
    }

    private static void writeDirectoryTexture(Path root, int color) throws IOException {
        Path texture = root.resolve(InstalledCableTexture.RESOURCE);
        Files.createDirectories(texture.getParent());
        Files.write(texture, png(color));
    }

    private static Texture texture(int color) throws IOException {
        return Texture.from(InstalledCableTexture.KEY, image(color));
    }

    private static byte[] png(int color) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image(color), "png", output);
        return output.toByteArray();
    }

    private static BufferedImage image(int color) {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, color);
            }
        }
        return image;
    }
}
