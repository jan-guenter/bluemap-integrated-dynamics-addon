/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.color.BlockColorCalculator;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Element;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Face;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Model;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.math.Color;
import de.bluecolored.bluemap.core.util.math.VectorM3f;
import de.bluecolored.bluemap.core.world.BlockState;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Emits the source model's directional facade sprites as clipped face layers. */
final class FacadeEmitter {

    private static final float CABLE_APERTURE = 0.25F;

    private final ResourcePack resourcePack;
    private final PrimitiveEmitter primitives;
    private final BlockColorCalculator blockColorCalculator;

    FacadeEmitter(ResourcePack resourcePack, PrimitiveEmitter primitives) {
        this.resourcePack = resourcePack;
        this.primitives = primitives;
        this.blockColorCalculator = resourcePack.createBlockColorCalculator();
    }

    boolean emit(
            MultipartSnapshot snapshot,
            BlockNeighborhood block,
            TileModelView target
    ) {
        MultipartSnapshot.FacadeState facade = snapshot.facade();
        if (facade == null) {
            return true;
        }
        BlockState worldState = new BlockState(facade.block(), facade.properties());
        de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState state =
                resourcePack.getBlockStates().get(facade.block());
        if (state == null) {
            return false;
        }
        List<Variant> variants = new ArrayList<>();
        state.forEach(worldState, block.getX(), block.getY(), block.getZ(), variants::add);
        if (variants.isEmpty()) {
            return false;
        }

        Color tint = blockColorCalculator.getBlockColor(block, worldState, new Color()).straight();
        int tintArgb = tint.getInt() | 0xFF00_0000;
        List<FacadeLayer> layers = new ArrayList<>();
        for (Variant variant : variants) {
            Model model = variant.getModel().getResource(resourcePack.getModels()::get);
            if (model == null || model.getElements() == null) {
                return false;
            }
            for (Element element : model.getElements()) {
                if (element == null) {
                    continue;
                }
                for (Map.Entry<Direction, Face> entry : element.getFaces().entrySet()) {
                    Face face = entry.getValue();
                    if (face == null || face.getCullface() == null) {
                        continue;
                    }
                    CableSide side = transform(face.getCullface(), variant);
                    Key texture = texture(model, face);
                    if (side == null || texture == null) {
                        return false;
                    }
                    int argb = face.getTintindex() >= 0 ? tintArgb : 0xFFFF_FFFF;
                    layers.add(new FacadeLayer(side, texture, argb));
                }
            }
        }
        if (layers.isEmpty()) {
            return false;
        }

        for (FacadeLayer layer : layers) {
            CableSide side = layer.side();
            PartCatalog.PartSpec part = partAt(snapshot, side);
            float width = part != null ? part.apertureWidth()
                    : snapshot.connected().contains(side) ? CABLE_APERTURE : 0F;
            float height = part != null ? part.apertureHeight()
                    : snapshot.connected().contains(side) ? CABLE_APERTURE : 0F;
            emitFace(block, target, layer.texture(), side, width, height, layer.argb());
        }
        return true;
    }

    private void emitFace(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            CableSide side,
            float holeWidth,
            float holeHeight,
            int argb
    ) {
        if (holeWidth <= 0F || holeHeight <= 0F) {
            primitives.facadeRectangle(block, target, texture, side,
                    0F, 0F, 1F, 1F, argb);
            return;
        }
        float u0 = (1F - holeWidth) * 0.5F;
        float u1 = 1F - u0;
        float v0 = (1F - holeHeight) * 0.5F;
        float v1 = 1F - v0;
        primitives.facadeRectangle(block, target, texture, side,
                0F, 0F, u0, 1F, argb);
        primitives.facadeRectangle(block, target, texture, side,
                u1, 0F, 1F, 1F, argb);
        primitives.facadeRectangle(block, target, texture, side,
                u0, 0F, u1, v0, argb);
        primitives.facadeRectangle(block, target, texture, side,
                u0, v1, u1, 1F, argb);
    }

    private static PartCatalog.PartSpec partAt(MultipartSnapshot snapshot, CableSide side) {
        for (MultipartSnapshot.MountedPart part : snapshot.parts()) {
            if (part.side() == side) {
                return part.spec();
            }
        }
        return null;
    }

    private Key texture(Model model, Face face) {
        if (face == null) {
            return null;
        }
        ResourcePath<Texture> key = face.getTexture().getTexturePath(model.getTextures()::get);
        return key != null && resourcePack.getTextures().get(key) != null ? key : null;
    }

    private static CableSide transform(Direction direction, Variant variant) {
        var source = direction.toVector();
        VectorM3f transformed = new VectorM3f(source.getX(), source.getY(), source.getZ());
        if (variant.isTransformed()) {
            transformed.rotateAndScale(variant.getTransformMatrix());
        }
        if (!Float.isFinite(transformed.x)
                || !Float.isFinite(transformed.y)
                || !Float.isFinite(transformed.z)) {
            return null;
        }
        int x = Math.round(transformed.x);
        int y = Math.round(transformed.y);
        int z = Math.round(transformed.z);
        if (Math.abs(transformed.x - x) > 1.0E-4F
                || Math.abs(transformed.y - y) > 1.0E-4F
                || Math.abs(transformed.z - z) > 1.0E-4F) {
            return null;
        }
        for (CableSide side : CableSide.values()) {
            if (side.x() == x && side.y() == y && side.z() == z) {
                return side;
            }
        }
        return null;
    }

    private record FacadeLayer(CableSide side, Key texture, int argb) {
    }
}
