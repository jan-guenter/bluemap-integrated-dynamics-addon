/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Element;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Face;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Model;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockProperties;
import de.bluecolored.bluemap.core.world.BlockState;
import io.github.janguenter.bluemap.integrateddynamics.activation.IntegratedDynamicsRuntime;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/** Resource routing for the single Integrated Dynamics multipart cable block. */
final class IntegratedDynamicsResourceExtension implements ResourcePackExtension {

    private static final Key CABLE = Key.parse("integrateddynamics:cable");
    private static final Key CABLE_TEXTURE = Key.parse("integrateddynamics:block/cable");
    private static final Key STONE_TEXTURE = Key.parse("minecraft:block/stone");
    private static final Key OAK_TEXTURE = Key.parse("minecraft:block/oak_planks");
    private static final Key SYNTHETIC = Key.parse("bluemap_integrateddynamics:cable");

    private final ResourcePack resourcePack;
    private final IntegratedDynamicsRuntime runtime;

    IntegratedDynamicsResourceExtension(
            ResourcePack resourcePack,
            IntegratedDynamicsRuntime runtime
    ) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.integrateddynamics.disabled")) {
            runtime.route().inactive("operator-disabled");
        } else {
            runtime.route().activate();
        }
    }

    @Override
    public Set<Key> collectUsedTextureKeys() {
        if (!runtime.route().isActive()) {
            return Set.of();
        }

        LinkedHashSet<Key> textures = new LinkedHashSet<>();
        textures.add(CABLE_TEXTURE);
        textures.add(STONE_TEXTURE);
        textures.add(OAK_TEXTURE);
        for (PartCatalog.PartSpec part : PartCatalog.all()) {
            Model model = resourcePack.getModels().get(part.model());
            if (model == null || model.getElements() == null) {
                continue;
            }
            for (Element element : model.getElements()) {
                if (element == null) {
                    continue;
                }
                for (Face face : element.getFaces().values()) {
                    ResourcePath<Texture> texture = face.getTexture()
                            .getTexturePath(model.getTextures()::get);
                    if (texture != null) {
                        textures.add(texture);
                    }
                }
            }
        }
        return Set.copyOf(textures);
    }

    @Override
    public void bake() {
        if (!runtime.route().isActive()) {
            return;
        }
        if (resourcePack.getTextures().get(CABLE_TEXTURE) == null) {
            runtime.route().inactive("cable-texture-missing");
            return;
        }
        for (PartCatalog.PartSpec part : PartCatalog.all()) {
            if (resourcePack.getModels().get(part.model()) == null) {
                runtime.route().inactive("part-model-missing");
                return;
            }
        }
        de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState dispatch =
                resourcePack.getBlockStates().get(SYNTHETIC);
        if (!validDispatch(dispatch)) {
            runtime.route().inactive("synthetic-dispatch-invalid");
            return;
        }
        System.out.println("BlueMap Integrated Dynamics add-on active: "
                + PartCatalog.size() + " neutral family part models.");
    }

    @Override
    public Key getBlockStateKey(Key key) {
        return runtime.route().isActive() && CABLE.equals(key) ? SYNTHETIC : key;
    }

    @Override
    public void getBlockProperties(BlockState blockState, BlockProperties.Builder builder) {
        if (runtime.route().isActive() && CABLE.equals(blockState.getId())) {
            builder.culling(false).occluding(false).cullingIdentical(false);
        }
    }

    private static boolean validDispatch(
            de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState state
    ) {
        if (state == null || state.getMultipart() != null) {
            return false;
        }
        Variants variants = state.getVariants();
        if (variants == null || variants.getDefaultVariant() == null) {
            return false;
        }
        VariantSet set = variants.getDefaultVariant();
        if (set.getVariants().length != 1) {
            return false;
        }
        Variant variant = set.getVariants()[0];
        return variant.getRenderer() == BlueMap522Adapter.renderer()
                && ResourcePack.MISSING_BLOCK_MODEL.equals(variant.getModel());
    }
}
