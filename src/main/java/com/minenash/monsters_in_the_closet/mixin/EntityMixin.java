package com.minenash.monsters_in_the_closet.mixin;


import com.minenash.monsters_in_the_closet.duck.MonsterInTheCloset;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
@SuppressWarnings("AbstractClassNeverImplemented")
public abstract class EntityMixin implements MonsterInTheCloset {
    @Unique
    private int glowTime = -1;
    
    @Inject(method = "baseTick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (glowTime >= 0)
            glowTime--;
        
        if (glowTime == 0)
            setGlowing(false);
    }
    
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    public void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (glowTime > 0)
            cir.setReturnValue(true);
    }
    
    @Unique
    @Override
    public int getGlowTime() {
        return this.glowTime;
    }
    
    @Unique
    @Override
    public void setGlowTime(int glowTime) {
        setGlowing(true);
        this.glowTime = glowTime;
    }
    
    @Shadow
    public abstract void setGlowing(boolean glowing);
}
