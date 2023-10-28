package net.cocoonmc.runtime.forge.mixin;

import net.cocoonmc.runtime.forge.annotation.Available;
import net.cocoonmc.runtime.forge.helper.ItemHelper;
import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Available("[1.18, )")
@Mixin(NetworkHooks.class)
public class ConnectionTypeMixin {

    @Inject(method = "handleClientLoginSuccess", at = @At("HEAD"), remap = false)
    private static void aw$updateConnectType(Connection manager, CallbackInfo cir) {
        ItemHelper.setEnableRedirect(NetworkHooks.isVanillaConnection(manager));
    }
}
