package macancraft.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MacanEntity extends TameableEntity implements Inventory {

    // ===== ИНВЕНТАРЬ =====
    private final DefaultedList<ItemStack> items =
            DefaultedList.ofSize(27, ItemStack.EMPTY);

    public MacanEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);

    }


    // ===== Inventory =====
    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.isOwner(player)
                && this.isAlive()
                && player.squaredDistanceTo(this) < 64.0;
    }

    @Override
    public void clear() {
        items.clear();
    }

    // ===== АТРИБУТЫ =====
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0); // ✅ авто-подъём
    }


    // ===== AI =====
    @Override
    protected void initGoals() {

        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));

        this.targetSelector.add(
                4,
                new ActiveTargetGoal<>(
                        this,
                        HostileEntity.class,
                        true
                )
        );

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(3, new SitGoal(this));
        this.goalSelector.add(5, new FollowOwnerGoal(this, 1.0D, 5.0F, 2.0F));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    // ===== ОБЯЗАТЕЛЬНЫЕ =====
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    // ===== ВЗАИМОДЕЙСТВИЕ =====
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isTamed() && stack.isOf(Items.BONE)) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            if (!this.getWorld().isClient) {
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setTamed(true, true);
                    this.setSitting(false);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }
            }
            return ActionResult.SUCCESS;
        }

        if (this.isTamed() && this.isOwner(player) && !this.getWorld().isClient) {
            this.setSitting(false);

            if (player.isSneaking()) {
                openInventory(player);
            } else {
                player.startRiding(this);
            }
            return ActionResult.CONSUME;
        }

        return super.interactMob(player, hand);
    }

    // ===== ЕЗДА + ПРЫЖОК =====
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof PlayerEntity;
    }

    @Override
    public void travel(Vec3d movementInput) {

        if (this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity player) {

            this.setYaw(player.getYaw());
            this.bodyYaw = this.getYaw();
            this.headYaw = this.getYaw();

            float forward = player.forwardSpeed;
            float sideways = player.sidewaysSpeed * 0.5F;

            this.setMovementSpeed(
                    (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)
            );

            super.travel(new Vec3d(sideways, movementInput.y, forward));
            return;
        }

        super.travel(movementInput);
    }


    // ===== GUI =====
    private void openInventory(PlayerEntity player) {
        player.openHandledScreen(
                new SimpleNamedScreenHandlerFactory(
                        (syncId, inv, p) ->
                                new GenericContainerScreenHandler(
                                        ScreenHandlerType.GENERIC_9X3,
                                        syncId,
                                        inv,
                                        this,
                                        3
                                ),
                        this.getDisplayName()
                )
        );
    }

    // ===== NBT =====
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (this.getWorld() != null) {
            RegistryWrapper.WrapperLookup lookup =
                    this.getWorld().getRegistryManager();

            Inventories.writeNbt(nbt, items, lookup);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (this.getWorld() != null) {
            RegistryWrapper.WrapperLookup lookup =
                    this.getWorld().getRegistryManager();

            Inventories.readNbt(nbt, items, lookup);
        }
    }

    // ===== ДРОП =====
    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.getWorld().isClient) {
            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    this.dropStack(stack);
                }
            }
        }
    }
}
