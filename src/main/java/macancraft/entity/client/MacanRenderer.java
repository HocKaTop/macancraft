package macancraft.entity.client;

import macancraft.Macancraft;
import macancraft.entity.MacanEntity;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class MacanRenderer extends LivingEntityRenderer<MacanEntity, PlayerEntityModel<MacanEntity>> {

    private static final Identifier TEXTURE =
            Identifier.of("macancraft", "textures/entity/macan.png");

    public MacanRenderer(EntityRendererFactory.Context ctx) {
        super(
                ctx,
                new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false),
                0.5f
        );
    }

    @Override
    public Identifier getTexture(MacanEntity entity) {
        return TEXTURE;
    }
}
