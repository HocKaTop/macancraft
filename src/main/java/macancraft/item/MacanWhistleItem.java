package macancraft.item;

import macancraft.entity.MacanEntity;
import macancraft.registry.ModEntities;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MacanWhistleItem extends Item {

    private static final String COMPANION_NBT_KEY = "MacanCompanion";

    public MacanWhistleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.success(stack);
        }

        return TypedActionResult.success(stack, summonCompanion((ServerWorld) world, stack, user).isAccepted());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        return summonCompanion((ServerWorld) world, context.getStack(), context.getPlayer());
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, net.minecraft.entity.LivingEntity entity, Hand hand) {
        ItemStack heldStack = user.getStackInHand(hand);
        if (!(entity instanceof MacanEntity macan)) {
            return ActionResult.PASS;
        }

        if (!macan.isTamed() || !macan.isOwner(user)) {
            return ActionResult.FAIL;
        }

        if (hasStoredCompanion(heldStack)) {
            return ActionResult.FAIL;
        }

        if (user.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        NbtCompound nbt = new NbtCompound();
        macan.writeNbt(nbt);
        setStoredCompanion(heldStack, nbt);
        macan.discard();
        return ActionResult.SUCCESS;
    }

    private ActionResult summonCompanion(ServerWorld world, ItemStack stack, PlayerEntity user) {
        if (user == null) {
            return ActionResult.FAIL;
        }

        NbtCompound storedNbt = getStoredCompanion(stack);
        if (storedNbt == null) {
            return ActionResult.FAIL;
        }

        MacanEntity macan = ModEntities.MACAN.create(world);
        if (macan == null) {
            return ActionResult.FAIL;
        }

        NbtCompound nbt = storedNbt.copy();
        nbt.remove("UUID");
        nbt.remove("Pos");
        nbt.remove("Motion");
        nbt.remove("Rotation");
        nbt.remove("Leash");
        nbt.remove("Passengers");

        macan.readNbt(nbt);
        macan.refreshPositionAndAngles(user.getX(), user.getY(), user.getZ(), user.getYaw(), user.getPitch());
        macan.setSitting(false);
        macan.setInSittingPose(false);
        if (!world.spawnEntity(macan)) {
            return ActionResult.FAIL;
        }

        clearStoredCompanion(stack);
        return ActionResult.SUCCESS;
    }

    private static boolean hasStoredCompanion(ItemStack stack) {
        return getStoredCompanion(stack) != null;
    }

    private static NbtCompound getStoredCompanion(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }

        NbtCompound root = customData.copyNbt();
        if (!root.contains(COMPANION_NBT_KEY)) {
            return null;
        }

        return root.getCompound(COMPANION_NBT_KEY);
    }

    private static void setStoredCompanion(ItemStack stack, NbtCompound companionNbt) {
        NbtCompound root = new NbtCompound();
        root.put(COMPANION_NBT_KEY, companionNbt);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(root));
    }

    private static void clearStoredCompanion(ItemStack stack) {
        stack.remove(DataComponentTypes.CUSTOM_DATA);
    }
}
