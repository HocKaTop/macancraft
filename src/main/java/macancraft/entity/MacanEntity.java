package macancraft.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MacanEntity extends TameableEntity {

    public MacanEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    // === АТРИБУТЫ ===
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30);
    }

    // === AI ===
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0, 5.0F, 2.0F));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));

        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
    }

    // === ОБЯЗАТЕЛЬНО ДЛЯ AnimalEntity ===
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BEEF); // можешь поставить false если не нужен размножение
    }

    // === ОБЯЗАТЕЛЬНО ДЛЯ PassiveEntity ===
    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null; // если не хочешь размножение — это нормально
    }

    // === ПРИРУЧЕНИЕ ===
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isTamed() && stack.isOf(Items.BONE)) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            if (this.random.nextInt(3) == 0) {
                this.setOwner(player);
                this.setTamed(true, true);
                this.getWorld().sendEntityStatus(this, (byte) 7);
            } else {
                this.getWorld().sendEntityStatus(this, (byte) 6);
            }
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }
}
