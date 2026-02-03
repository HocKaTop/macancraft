package macancraft;

import macancraft.block.ModBlocks;
import macancraft.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Macancraft implements ModInitializer {
	public static final String MOD_ID = "macancraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		LOGGER.info("Hello Fabric world!");
	}
}