/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.integrateddynamics.activation.IntegratedDynamicsRuntime;

/** Resource-pack extension factory registered before resource loading. */
final class IntegratedDynamicsResourceExtensionType
        implements ResourcePack.Extension<IntegratedDynamicsResourceExtension> {

    private static final Key KEY = Key.parse("bluemap_integrateddynamics:prototype");

    private final IntegratedDynamicsRuntime runtime;

    IntegratedDynamicsResourceExtensionType(IntegratedDynamicsRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public IntegratedDynamicsResourceExtension create(ResourcePack pack) {
        return new IntegratedDynamicsResourceExtension(pack, runtime);
    }
}
