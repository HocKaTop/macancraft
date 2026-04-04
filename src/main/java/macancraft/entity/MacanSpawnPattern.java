package macancraft.entity;

import macancraft.block.ModBlocks;
import macancraft.registry.ModEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MacanSpawnPattern {

    public static void trySpawn(World world, BlockPos pumpkinPos) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (!isPumpkinBlock(world, pumpkinPos)) return;

        BlockPos middlePos = pumpkinPos.down();
        BlockPos basePos = pumpkinPos.down(2);
        if (!world.getBlockState(middlePos).isOf(ModBlocks.MACAN_BLOCK)) return;
        if (!world.getBlockState(basePos).isOf(ModBlocks.MACAN_BLOCK)) return;

        world.setBlockState(pumpkinPos, Blocks.AIR.getDefaultState(), 2);
        world.setBlockState(middlePos, Blocks.AIR.getDefaultState(), 2);
        world.setBlockState(basePos, Blocks.AIR.getDefaultState(), 2);

        ModEntities.MACAN.spawn(
                serverWorld,
                middlePos,
                SpawnReason.TRIGGERED
        );
    }

    private static boolean isPumpkinBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.PUMPKIN)
                || world.getBlockState(pos).isOf(Blocks.CARVED_PUMPKIN);
    }
}
