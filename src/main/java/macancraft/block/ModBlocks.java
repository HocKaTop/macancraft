package macancraft.block;

import macancraft.Macancraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block MACAN_BLOCK = registerBlock("macan_block",
            new Block(AbstractBlock.Settings.create().strength(1f)
                    .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)));


    public static final Block MACAN_ORE_BLOCK = registerBlock("macan_ore_block",
            new Block(AbstractBlock.Settings.create().strength(1f)
                    .requiresTool()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Macancraft.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Macancraft.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Macancraft.LOGGER.info("Registering Mod Blocks for " + Macancraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.MACAN_BLOCK);
            entries.add(ModBlocks.MACAN_ORE_BLOCK);
        });
    }
}