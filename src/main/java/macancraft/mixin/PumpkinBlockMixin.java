package macancraft.mixin;

import macancraft.entity.MacanSpawnPattern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class PumpkinBlockMixin {

    @Inject(
            method = "onPlaced",
            at = @At("TAIL")
    )
    private void macancraft$onPlaced(
            World world,
            BlockPos pos,
            BlockState state,
            net.minecraft.entity.LivingEntity placer,
            net.minecraft.item.ItemStack itemStack,
            CallbackInfo ci
    ) {
        if (world.isClient) return;

        // ✅ только если это тыква
        if (state.isOf(Blocks.PUMPKIN) || state.isOf(Blocks.CARVED_PUMPKIN)) {
            MacanSpawnPattern.trySpawn(world, pos);
        }
    }
}
