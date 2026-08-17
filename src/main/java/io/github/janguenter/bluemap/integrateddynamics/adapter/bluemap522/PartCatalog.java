/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Key;

import java.util.LinkedHashMap;
import java.util.Map;

/** Stable neutral model and physical footprint for the 48 installed family parts. */
final class PartCatalog {

    private static final Map<String, PartSpec> PARTS = create();

    private PartCatalog() {
    }

    static PartSpec get(String id) {
        return PARTS.get(id);
    }

    static int size() {
        return PARTS.size();
    }

    static Iterable<PartSpec> all() {
        return PARTS.values();
    }

    private static Map<String, PartSpec> create() {
        Map<String, PartSpec> parts = new LinkedHashMap<>();

        add(parts, "integrateddynamics", 0.3125F, 0.625F, false,
                "audio_reader", "block_reader", "entity_reader", "extradimensional_reader",
                "fluid_reader", "inventory_reader", "machine_reader", "network_reader",
                "redstone_reader", "world_reader");
        add(parts, "integrateddynamics", 0.3125F, 0.25F, true,
                "audio_writer", "effect_writer", "entity_writer", "machine_writer",
                "inventory_writer", "redstone_writer");
        add(parts, "integrateddynamics", 0.1875F, 0.625F, false,
                "static_light_panel");
        add(parts, "integrateddynamics", 0.1875F, 0.625F, true,
                "dynamic_light_panel", "display_panel");
        add(parts, "integrateddynamics", 0.3125F, 0.5F, true,
                "connector_mono_directional");
        add(parts, "integrateddynamics", 0.3125F, 0.625F, true,
                "connector_omni_directional");

        add(parts, "integratedtunnels", 0.25F, 0.375F, false,
                "interface_energy", "interface_fluid", "interface_item");
        add(parts, "integratedtunnels", 0.25F, 0.375F, true,
                "interface_filter_energy", "interface_filter_fluid", "interface_filter_item",
                "importer_energy", "exporter_energy", "importer_item", "exporter_item",
                "importer_fluid", "exporter_fluid");
        add(parts, "integratedtunnels", 0.1875F, 0.625F, true,
                "importer_world_energy", "exporter_world_energy", "importer_world_item",
                "exporter_world_item", "importer_world_fluid", "exporter_world_fluid",
                "importer_world_block", "exporter_world_block", "player_simulator");

        add(parts, "integratedterminals", 0.1875F, 0.625F, false,
                "terminal_storage", "terminal_crafting_job");
        add(parts, "integratedcrafting", 0.1875F, 0.625F, false,
                "interface_crafting", "interface_crafting_attuned");
        add(parts, "integratedcrafting", 0.3125F, 0.25F, true,
                "crafting_writer");
        add(parts, "integratedscripting", 0.1875F, 0.625F, false,
                "terminal_scripting");

        return Map.copyOf(parts);
    }

    private static void add(
            Map<String, PartSpec> target,
            String namespace,
            float depth,
            float aperture,
            boolean inactive,
            String... names
    ) {
        for (String name : names) {
            String id = namespace + ":" + name;
            String model = namespace + ":block/part_" + name + (inactive ? "_inactive" : "");
            target.put(id, new PartSpec(Key.parse(model), depth, aperture, aperture));
        }
    }

    record PartSpec(Key model, float depth, float apertureWidth, float apertureHeight) {
    }
}
