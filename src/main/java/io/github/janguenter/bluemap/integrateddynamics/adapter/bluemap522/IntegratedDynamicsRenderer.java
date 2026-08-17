/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.MaxCapacityReachedException;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.math.Color;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;
import io.github.janguenter.bluemap.integrateddynamics.activation.IntegratedDynamicsRuntime;

/** Renders stable cable topology, neutral family parts, and persisted facades. */
final class IntegratedDynamicsRenderer implements BlockRenderer {

    private static final Key CABLE_TEXTURE = Key.parse("integrateddynamics:block/cable");
    private static final float MIN = 0.375F;
    private static final float MAX = 0.625F;

    private final IntegratedDynamicsRuntime runtime;
    private final MultipartSnapshotDecoder decoder = new MultipartSnapshotDecoder();
    private final JsonModelEmitter json;
    private final PrimitiveEmitter primitives;
    private final FacadeEmitter facades;
    private final BoundedDiagnostics diagnostics = new BoundedDiagnostics();

    IntegratedDynamicsRenderer(
            ResourcePack resourcePack,
            TextureGallery textures,
            RenderSettings renderSettings,
            IntegratedDynamicsRuntime runtime
    ) {
        this.runtime = runtime;
        this.json = new JsonModelEmitter(resourcePack, textures);
        this.primitives = new PrimitiveEmitter(textures);
        this.facades = new FacadeEmitter(resourcePack, primitives);
    }

    @Override
    public void render(
            BlockNeighborhood block,
            Variant original,
            TileModelView target,
            Color mapColor
    ) {
        int start = target.getStart();
        try {
            MultipartBlockEntityData data = block.getBlockEntity()
                    instanceof MultipartBlockEntityData found ? found : null;
            MultipartSnapshot snapshot = decoder.decode(data);
            if (snapshot.realCable()) {
                cable(snapshot, block, target);
            }
            for (MultipartSnapshot.MountedPart part : snapshot.parts()) {
                if (!json.emit(
                        part.spec().model(),
                        block,
                        target,
                        part.side().xRotation(),
                        part.side().yRotation(),
                        0F
                )) {
                    diagnostics.report("part-model-missing");
                }
            }
            if (!facades.emit(snapshot, block, target)) {
                diagnostics.report("facade-model-unsupported");
            }
        } catch (MaxCapacityReachedException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            target.getTileModel().reset(start);
            target.initialize(start);
            runtime.route().inactive("render-exception");
            diagnostics.report("render-exception");
            primitives.box(block, target, CABLE_TEXTURE, MIN, MIN, MIN, MAX, MAX, MAX);
        }
    }

    private void cable(
            MultipartSnapshot snapshot,
            BlockNeighborhood block,
            TileModelView target
    ) {
        primitives.box(block, target, CABLE_TEXTURE, MIN, MIN, MIN, MAX, MAX, MAX);
        for (CableSide side : CableSide.values()) {
            PartCatalog.PartSpec part = partAt(snapshot, side);
            if (!snapshot.connected().contains(side) && part == null) {
                continue;
            }
            float depth = snapshot.connected().contains(side) ? 0F : part.depth();
            switch (side) {
                case DOWN -> primitives.box(block, target, CABLE_TEXTURE,
                        MIN, depth, MIN, MAX, MIN, MAX);
                case UP -> primitives.box(block, target, CABLE_TEXTURE,
                        MIN, MAX, MIN, MAX, 1F - depth, MAX);
                case NORTH -> primitives.box(block, target, CABLE_TEXTURE,
                        MIN, MIN, depth, MAX, MAX, MIN);
                case SOUTH -> primitives.box(block, target, CABLE_TEXTURE,
                        MIN, MIN, MAX, MAX, MAX, 1F - depth);
                case WEST -> primitives.box(block, target, CABLE_TEXTURE,
                        depth, MIN, MIN, MIN, MAX, MAX);
                case EAST -> primitives.box(block, target, CABLE_TEXTURE,
                        MAX, MIN, MIN, 1F - depth, MAX, MAX);
            }
        }
    }

    private static PartCatalog.PartSpec partAt(MultipartSnapshot snapshot, CableSide side) {
        for (MultipartSnapshot.MountedPart part : snapshot.parts()) {
            if (part.side() == side) {
                return part.spec();
            }
        }
        return null;
    }
}
