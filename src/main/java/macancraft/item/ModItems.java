package macancraft.item;
import macancraft.Macancraft;
import macancraft.registry.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item MACAN_ORE_BLOCK = registerItem("macan_ore", new Item(new Item.Settings()));
    public static final Item RAW_MACAN_ORE = registerItem("raw_macan_ore", new Item(new Item.Settings()));
    public static final Item MACAN_DUST = registerItem("macan_dust", new Item(new Item.Settings()));
    public static final Item MACAN_WHISTLE = registerItem("macan_whistle", new MacanWhistleItem(new Item.Settings().maxCount(1)));
    public static final Item MACAN_SPAWN_EGG = registerItem(
            "macan_spawn_egg",
            new SpawnEggItem(ModEntities.MACAN, 0x7A5A3A, 0xDCC5A1, new Item.Settings())
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Macancraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Macancraft.LOGGER.info("Registering Mod Items for " + Macancraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(MACAN_ORE_BLOCK);
            entries.add(RAW_MACAN_ORE);
            entries.add(MACAN_DUST);
            entries.add(MACAN_WHISTLE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(MACAN_SPAWN_EGG);
        });
    }
}
