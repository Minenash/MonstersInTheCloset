package net.fabricmc.example.mixin;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.MonstersInTheCloset;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {


    @Inject(method = "trySleep", at = @At("RETURN"))
    public void highlightMobs(BlockPos pos, CallbackInfoReturnable info) {
        World world = MonstersInTheCloset.world;
        Optional<PlayerEntity.SleepFailureReason> reason = ((Either<PlayerEntity.SleepFailureReason, Unit>) info.getReturnValue()).left();
        if (world != null && reason.isPresent() && reason.get() == PlayerEntity.SleepFailureReason.NOT_SAFE) {

            Vec3d vec3d = Vec3d.ofBottomCenter(pos);
            List<HostileEntity> list = world.getEntitiesByClass(
                    HostileEntity.class,
                    new Box(vec3d.getX() - 8.0D, vec3d.getY() - 5.0D, vec3d.getZ() - 8.0D, vec3d.getX() + 8.0D, vec3d.getY() + 5.0D, vec3d.getZ() + 8.0D),
                    (hostileEntity) -> hostileEntity.isAngryAt((PlayerEntity) (Object) this)
            );

            for (HostileEntity entity : list)
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, true, false));

        }
    }

}
