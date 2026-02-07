package macancraft;

import macancraft.entity.client.MacanModel;
import macancraft.entity.client.MacanRenderer;
import macancraft.entity.client.ModModelLayers;
import macancraft.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MacancraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(
                ModEntities.MACAN,
                MacanRenderer::new
        );

        EntityModelLayerRegistry.registerModelLayer(
                ModModelLayers.MACAN,
                MacanModel::getTexturedModelData
        );
    }
}
