package com.minenash.monsters_in_the_closet.mixin;


import com.minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(BedBlock.class)
public class BedBlockMixin {
    
    @Unique
    private static BlockPos blockPos = null;
    
    @Inject(method = "method_19283",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendMessage(Lnet/minecraft/text/Text;Z)V"))
    private static void trySleepButEjected(PlayerEntity player, PlayerEntity.SleepFailureReason reason, CallbackInfo info) {
        if (reason != PlayerEntity.SleepFailureReason.NOT_SAFE)
            return;
        
        
        if (blockPos != null) {
            Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);
            List<HostileEntity> list = player.world.getEntitiesByClass(
                    HostileEntity.class,
                    new Box(vec3d.getX() - 8.0D, vec3d.getY() - 5.0D, vec3d.getZ() - 8.0D, vec3d.getX() + 8.0D, vec3d.getY() + 5.0D,
                            vec3d.getZ() + 8.0D),
                    (hostileEntity) -> hostileEntity.isAngryAt(player)
                                                                      );
            
            if (!list.isEmpty()) {
                MonstersInTheCloset.duration = 60;
                MonstersInTheCloset.list = list;
            }
        }
        
        
    }
    
    @Inject(method = "onUse", at = @At("HEAD"))
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result,
                      CallbackInfoReturnable<ActionResult> info) {
        blockPos = pos;
    }
    
}
