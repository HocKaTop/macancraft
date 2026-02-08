package macancraft;

import macancraft.block.ModBlocks;
import macancraft.entity.MacanSpawnPattern;
import macancraft.item.ModItems;
import macancraft.registry.ModEntities;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Macancraft implements ModInitializer {
	public static final String MOD_ID = "macancraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntities.register();
		LOGGER.info("Hello Fabric world!");

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.isClient) return ActionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);

			// если поставили тыкву
			if (state.isOf(Blocks.PUMPKIN)) {
				MacanSpawnPattern.trySpawn(world, pos);
			}

			return ActionResult.PASS;
		});

	}
}