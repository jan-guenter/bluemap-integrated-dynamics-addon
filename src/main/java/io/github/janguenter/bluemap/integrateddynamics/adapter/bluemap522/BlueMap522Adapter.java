/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Keyed;
import de.bluecolored.bluemap.core.util.Registry;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import io.github.janguenter.bluemap.integrateddynamics.activation.IntegratedDynamicsRuntime;

/** BlueMap 5.22 internal ABI registration boundary. */
public final class BlueMap522Adapter {

    private static final IntegratedDynamicsRuntime RUNTIME = IntegratedDynamicsRuntime.INSTANCE;
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            de.bluecolored.bluemap.core.util.Key.parse("bluemap_integrateddynamics:cable"),
            (pack, gallery, settings) ->
                    new IntegratedDynamicsRenderer(pack, gallery, settings, RUNTIME)
    );
    private static final ResourcePack.Extension<IntegratedDynamicsResourceExtension> EXTENSION =
            new IntegratedDynamicsResourceExtensionType(RUNTIME);
    private static final BlockEntityType MULTIPART = new BlockEntityType.Impl(
            de.bluecolored.bluemap.core.util.Key.parse("integrateddynamics:multipart_ticking"),
            MultipartBlockEntityData.class
    );

    private BlueMap522Adapter() {
    }

    public static synchronized boolean install() {
        if (!canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !canRegister(BlockEntityType.REGISTRY, MULTIPART)) {
            RUNTIME.disable("registry-collision");
            return false;
        }
        boolean installed = register(BlockRendererType.REGISTRY, RENDERER)
                && register(ResourcePack.Extension.REGISTRY, EXTENSION)
                && register(BlockEntityType.REGISTRY, MULTIPART);
        if (!installed) {
            RUNTIME.disable("registry-collision");
        }
        return installed;
    }

    static BlockRendererType renderer() {
        return RENDERER;
    }

    private static <T extends Keyed> boolean canRegister(Registry<T> registry, T candidate) {
        T existing = registry.get(candidate.getKey());
        return existing == null || existing == candidate;
    }

    private static <T extends Keyed> boolean register(Registry<T> registry, T candidate) {
        T existing = registry.get(candidate.getKey());
        if (existing == null) {
            registry.register(candidate);
            existing = registry.get(candidate.getKey());
        }
        return existing == candidate;
    }
}
