package com.minenash.monsters_in_the_closet.mixin;


import com.minenash.monsters_in_the_closet.duck.MonsterInTheCloset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    
    @SuppressWarnings("InstanceofIncompatibleInterface")
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void showOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity.isGlowingLocal())
            info.setReturnValue(true);
        
        if (entity instanceof MonsterInTheCloset closetedMonster && closetedMonster.getGlowTime() > 0)
            info.setReturnValue(true); // Technically unneeded, but /shrug
    }
    
}
