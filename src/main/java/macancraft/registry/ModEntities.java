package macancraft.registry;

import macancraft.Macancraft;
import macancraft.entity.MacanEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<MacanEntity> MACAN =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(Macancraft.MOD_ID, "macan"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.CREATURE,
                                    MacanEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                            .build()
            );

    public static void register() {
        FabricDefaultAttributeRegistry.register(
                MACAN,
                MacanEntity.createAttributes()
        );
    }
}
