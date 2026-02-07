package macancraft.entity.client;

import macancraft.Macancraft;
import macancraft.entity.MacanEntity;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.Identifier;

public class MacanRenderer extends MobEntityRenderer<MacanEntity, MacanModel<MacanEntity>> {
    private static final Identifier TEXTURE =
            Identifier.of(Macancraft.MOD_ID, "textures/entity/macan.png");

    public MacanRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new MacanModel<>(context.getPart(ModModelLayers.MACAN)),
                0.5f
        );
    }

    @Override
    public Identifier getTexture(MacanEntity entity) {
        return TEXTURE;
    }
}
