package com.minenash.monsters_in_the_closet.mixin;


import com.minenash.monsters_in_the_closet.duck.MonsterInTheCloset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    
    @Shadow
    @Final
    private MinecraftClient client;
    
    @Inject(method = "onGameMessage",
            at = @At(value = "INVOKE",
                     shift = At.Shift.BEFORE,
                     target = "Lnet/minecraft/client/network/message/MessageHandler;onGameMessage(Lnet/minecraft/text/Text;Z)V")
    )
    @SuppressWarnings("CastToIncompatibleInterface")
    private void interceptDangerousSleepMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (client.player == null || client.world == null)
            return; // Should never happen, as when processing game messages, player should be in a world.
        
        if (!packet.overlay())
            return; // Overlay means it's displayed on the actionbar. The sleep message is always displayed on the action bar.
        
        if (!(packet.content().getContent() instanceof TranslatableTextContent translatableText))
            return; // If the content is not an instance of TranslatableTextContent, then return
        
        if (!"block.minecraft.bed.not_safe".equals(translatableText.getKey()))
            return; // If the translatable component is not "block.minecraft.bed.not_safe", then this isn't the game event we're looking for
        
        Vec3d vec3d = Vec3d.ofBottomCenter(client.player.getBlockPos());
        List<HostileEntity> list = client.world.getEntitiesByClass(
                HostileEntity.class,
                new Box(
                        vec3d.add(-8.0, -5.0, -8.0),
                        vec3d.add(8.0, 5.0, 8.0)
                ),
                hostileEntity -> hostileEntity.isAngryAt(client.player)
                                                                  );
        
        for (HostileEntity entity : list) {
            ((MonsterInTheCloset) entity).setGlowTime(60);
        }
    }
}
