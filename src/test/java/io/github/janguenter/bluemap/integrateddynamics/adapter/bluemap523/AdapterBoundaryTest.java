/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;
import io.github.janguenter.bluemap.addon.render.core.adapter.bluemap523.FaceLighting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdapterBoundaryTest {

    @Test
    void usesSharedFaceLightingWithoutLocalCopy() {
        assertEquals(
                "io.github.janguenter.bluemap.addon.render.core.adapter.bluemap523."
                        + "FaceLighting",
                FaceLighting.class.getName()
        );
        assertThrows(ClassNotFoundException.class, () -> Class.forName(
                "io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523."
                        + "FaceLighting"
        ));
    }

    @Test
    void usesSharedAdapterHelpersWithoutLocalCopies() {
        assertInstanceOf(ResourceExtensionType.class, BlueMap523Adapter.extension());
        assertEquals(
                Key.parse("bluemap_integrateddynamics:prototype"),
                BlueMap523Adapter.extension().getKey()
        );
        assertInstanceOf(
                IntegratedDynamicsResourceExtension.class,
                BlueMap523Adapter.extension().create(null)
        );
        assertThrows(ClassNotFoundException.class, () -> Class.forName(
                "io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523."
                        + "AdapterCompatibility"
        ));
        assertThrows(ClassNotFoundException.class, () -> Class.forName(
                "io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523."
                        + "IntegratedDynamicsResourceExtensionType"
        ));
    }
}
