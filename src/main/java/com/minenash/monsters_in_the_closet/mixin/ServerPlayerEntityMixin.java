package com.minenash.monsters_in_the_closet.mixin;


import com.minenash.monsters_in_the_closet.duck.MonsterInTheCloset;
import com.minenash.monsters_in_the_closet.duck.PlayerInTheCloset;
import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;


@Environment(EnvType.SERVER)
@Mixin(ServerPlayerEntity.class)
@SuppressWarnings("AbstractClassNeverImplemented")
public abstract class ServerPlayerEntityMixin extends Entity implements PlayerInTheCloset {
    @Unique
    private BlockPos lastSleepPos = null;
    
    ServerPlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "trySleep", at = @At("RETURN"))
    @SuppressWarnings("CastToIncompatibleInterface")
    public void highlightMobs(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> info) {
        if (info.getReturnValue() == null)
            return; // Should never happen, since we're injecting at return
        
        Optional<PlayerEntity.SleepFailureReason> reason = info.getReturnValue().left();
        
        if (world == null)
            return; // should never happen, but check is for safety
        if (reason.isEmpty())
            return; // No failure reason provided
        if (reason.get() != PlayerEntity.SleepFailureReason.NOT_SAFE)
            return; // Not the reason we're looking for
        
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
        List<HostileEntity> list = world.getEntitiesByClass(
                HostileEntity.class,
                new Box(
                        vec3d.add(-8.0, -5.0, -8.0),
                        vec3d.add(8.0, 5.0, 8.0)
                ),
                (hostileEntity) -> hostileEntity.isAngryAt((PlayerEntity) (Object) this)
                                                           );
        
        for (HostileEntity entity : list) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, true, false));
            ((MonsterInTheCloset) entity).setGlowTime(60);
        }
    }
    
    @Unique
    @Override
    public BlockPos getLastSleepPos() {
        return this.lastSleepPos;
    }
    
    @Unique
    @Override
    public void setLastSleepPos(BlockPos lastSleepPos) {
        this.lastSleepPos = lastSleepPos;
    }
}
