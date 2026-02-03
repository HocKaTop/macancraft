package macancraft.item;
import macancraft.Macancraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item MACAN_ORE_BLOCK = registerItem("macan_ore", new Item(new Item.Settings()));
    public static final Item RAW_MACAN_ORE = registerItem("raw_macan_ore", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Macancraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Macancraft.LOGGER.info("Registering Mod Items for " + Macancraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(MACAN_ORE_BLOCK);
            entries.add(RAW_MACAN_ORE);
        });
    }
}