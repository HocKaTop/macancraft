package macancraft.entity;

import macancraft.block.ModBlocks;
import macancraft.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.SpawnReason;
import java.util.function.Predicate;

public class MacanSpawnPattern {

    private static BlockPattern pattern;

    public static BlockPattern getPattern() {
        if (pattern == null) {
            pattern = BlockPatternBuilder.start()
                    .aisle(
                            "^",
                            "#",
                            "#"
                    )
                    .where('^', block(Blocks.PUMPKIN))
                    .where('#', block(ModBlocks.MACAN_BLOCK))
                    .build();
        }
        return pattern;
    }

    private static Predicate<CachedBlockPosition> block(net.minecraft.block.Block block) {
        return pos -> pos.getBlockState().isOf(block);
    }

    public static void trySpawn(World world, BlockPos pumpkinPos) {
        if (!(world instanceof ServerWorld serverWorld)) return;

        // 👇 ищем под тыквой
        BlockPos checkPos = pumpkinPos.down();

        var result = getPattern().searchAround(world, checkPos);
        if (result == null) return;

        // очистка блоков
        for (int i = 0; i < getPattern().getHeight(); i++) {
            BlockPos p = result.translate(0, i, 0).getBlockPos();
            world.setBlockState(p, Blocks.AIR.getDefaultState(), 2);
        }

        // спавн существа
        BlockPos spawnPos = result.translate(0, 1, 0).getBlockPos();

        ModEntities.MACAN.spawn(
                serverWorld,
                spawnPos,
                SpawnReason.TRIGGERED
        );
    }

}
