/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.util.Key;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Permissive decoder for the stable visual subset of CyclopsCore NBT. */
final class MultipartSnapshotDecoder {

    MultipartSnapshot decode(MultipartBlockEntityData data) {
        if (data == null) {
            return new MultipartSnapshot(true, Set.of(), List.of(), null);
        }

        EnumSet<CableSide> connected = directions(data.connected());
        connected.removeAll(directions(data.forceDisconnected()));

        List<MultipartSnapshot.MountedPart> parts = new ArrayList<>();
        Object rawParts = map(data.partContainer()).get("parts");
        if (rawParts instanceof List<?> list) {
            for (Object rawPart : list) {
                Map<?, ?> entry = map(rawPart);
                String type = string(entry.get("__partType"));
                CableSide side = CableSide.byName(string(entry.get("__side")));
                PartCatalog.PartSpec spec = PartCatalog.get(type);
                if (side != null && spec != null) {
                    parts.add(new MultipartSnapshot.MountedPart(side, spec));
                }
            }
        }

        return new MultipartSnapshot(
                truthy(data.realCable(), true),
                Set.copyOf(connected),
                List.copyOf(parts),
                facade(data.facadeBlockTag())
        );
    }

    private static EnumSet<CableSide> directions(Object raw) {
        EnumSet<CableSide> result = EnumSet.noneOf(CableSide.class);
        Object entries = map(raw).get("map");
        if (!(entries instanceof List<?> list)) {
            return result;
        }
        for (Object rawEntry : list) {
            Map<?, ?> entry = map(rawEntry);
            Object key = entry.get("key");
            CableSide side = key instanceof Number number
                    ? CableSide.byOrdinal(number.intValue()) : null;
            if (side != null && truthy(entry.get("value"), false)) {
                result.add(side);
            }
        }
        return result;
    }

    private static MultipartSnapshot.FacadeState facade(Object raw) {
        Map<?, ?> tag = map(raw);
        String name = string(tag.get("Name"));
        if (!name.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
            return null;
        }
        Map<String, String> properties = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map(tag.get("Properties")).entrySet()) {
            String key = string(entry.getKey());
            String value = string(entry.getValue());
            if (!key.isBlank() && !value.isBlank()) {
                properties.put(key, value);
            }
        }
        return new MultipartSnapshot.FacadeState(Key.parse(name), Map.copyOf(properties));
    }

    private static boolean truthy(Object value, boolean fallback) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() != 0;
        }
        return fallback;
    }

    private static String string(Object value) {
        return value instanceof String text ? text : "";
    }

    private static Map<?, ?> map(Object value) {
        return value instanceof Map<?, ?> found ? found : Map.of();
    }
}
