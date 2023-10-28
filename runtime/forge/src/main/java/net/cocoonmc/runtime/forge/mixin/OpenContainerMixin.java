package net.cocoonmc.runtime.forge.mixin;

import net.cocoonmc.runtime.forge.annotation.Available;
import net.cocoonmc.runtime.forge.helper.MenuMixinHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PlayMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Available("[1.18, )")
@Mixin(PlayMessages.OpenContainer.class)
public class OpenContainerMixin {

    @ModifyVariable(method = "decode", at = @At("HEAD"), argsOnly = true, remap = false)
    private static FriendlyByteBuf aw$decode(FriendlyByteBuf buffer) {
        return MenuMixinHelper.redirect(buffer);
    }
}
