package macancraft.entity.client;

import macancraft.Macancraft;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {

    public static final EntityModelLayer MACAN =
            new EntityModelLayer(
                    Identifier.of(Macancraft.MOD_ID, "macan"),
                    "main"
            );
}
