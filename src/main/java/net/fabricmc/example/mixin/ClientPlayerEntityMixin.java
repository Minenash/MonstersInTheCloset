package net.fabricmc.example.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.example.MonstersInTheCloset;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo  _info) {

        if (MonstersInTheCloset.duration == 0) {
            for (HostileEntity entity : MonstersInTheCloset.list) {
                entity.setGlowing(false);
            }
        }

        if (MonstersInTheCloset.duration >= 0)
            MonstersInTheCloset.duration--;

    }

}
