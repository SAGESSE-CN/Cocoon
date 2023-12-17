package net.cocoonmc.runtime.client.mixin;

import net.cocoonmc.runtime.client.helper.PacketHelper;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class NetworkMixin {

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void cocoon$handleCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (PacketHelper.test(packet.getIdentifier())) {
            ClientPacketListener listener = ClientPacketListener.class.cast(this);
            PacketHelper.handle(listener.getConnection(), packet.getData());
            ci.cancel();
        }
    }
}
