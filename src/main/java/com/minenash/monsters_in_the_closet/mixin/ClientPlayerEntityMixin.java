package com.minenash.monsters_in_the_closet.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo  _info) {

        if (MonstersInTheCloset.duration == 0)
            MonstersInTheCloset.list = new ArrayList<>();

        if (MonstersInTheCloset.duration >= 0)
            MonstersInTheCloset.duration--;

    }

}
