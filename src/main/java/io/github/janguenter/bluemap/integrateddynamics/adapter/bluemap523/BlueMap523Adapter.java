/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.RegistryGuard;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;
import io.github.janguenter.bluemap.integrateddynamics.activation.IntegratedDynamicsRuntime;

/** Exact BlueMap 5.23 feature-backport registration boundary. */
public final class BlueMap523Adapter {

    private static final IntegratedDynamicsRuntime RUNTIME = IntegratedDynamicsRuntime.INSTANCE;
    private static final Key EXTENSION_KEY =
            Key.parse("bluemap_integrateddynamics:prototype");
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            de.bluecolored.bluemap.core.util.Key.parse("bluemap_integrateddynamics:cable"),
            (pack, gallery, settings) ->
                    new IntegratedDynamicsRenderer(pack, gallery, settings, RUNTIME)
    );
    private static final ResourcePack.Extension<IntegratedDynamicsResourceExtension> EXTENSION =
            new ResourceExtensionType<>(
                    EXTENSION_KEY,
                    pack -> new IntegratedDynamicsResourceExtension(pack, RUNTIME)
            );
    private static final BlockEntityType MULTIPART = new BlockEntityType.Impl(
            de.bluecolored.bluemap.core.util.Key.parse("integrateddynamics:multipart_ticking"),
            MultipartBlockEntityData.class
    );

    private BlueMap523Adapter() {
    }

    public static synchronized boolean install() {
        if (!RegistryGuard.canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !RegistryGuard.canRegister(BlockEntityType.REGISTRY, MULTIPART)) {
            RUNTIME.disable("registry-collision");
            return false;
        }
        boolean installed = RegistryGuard.register(BlockRendererType.REGISTRY, RENDERER)
                && RegistryGuard.register(ResourcePack.Extension.REGISTRY, EXTENSION)
                && RegistryGuard.register(BlockEntityType.REGISTRY, MULTIPART);
        if (!installed) {
            RUNTIME.disable("registry-collision");
        }
        return installed;
    }

    static BlockRendererType renderer() {
        return RENDERER;
    }

    static ResourcePack.Extension<IntegratedDynamicsResourceExtension> extension() {
        return EXTENSION;
    }
}
