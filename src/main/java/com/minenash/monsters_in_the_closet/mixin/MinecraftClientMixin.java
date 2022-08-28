package com.minenash.monsters_in_the_closet.mixin;

import com.minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "hasOutline", at = @At("RETURN"), cancellable = true)
    private void showOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (MonstersInTheCloset.list.contains((HostileEntity) entity)) info.setReturnValue(true);
    }
}
