/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Key;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Stable visual state decoded from the multipart block entity. */
record MultipartSnapshot(
        boolean realCable,
        Set<CableSide> connected,
        List<MountedPart> parts,
        FacadeState facade
) {

    record MountedPart(CableSide side, PartCatalog.PartSpec spec) {
    }

    record FacadeState(Key block, Map<String, String> properties) {
    }
}
