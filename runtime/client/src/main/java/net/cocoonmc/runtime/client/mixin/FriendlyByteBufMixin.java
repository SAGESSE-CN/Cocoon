package net.cocoonmc.runtime.client.mixin;

import net.cocoonmc.runtime.client.helper.ItemHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {

    @Inject(method = "readItem", at = @At("RETURN"), cancellable = true)
    public void cocoon$readItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemHelper.unwrapStackIfNeeded(cir);
    }

    @ModifyVariable(method = "writeItem", at = @At("HEAD"), argsOnly = true)
    public ItemStack cocoon$writeItem(ItemStack itemStack) {
        return ItemHelper.wrapStackIfNeeded(itemStack);
    }

    @ModifyVariable(method = "writeItemStack", at = @At("HEAD"), argsOnly = true, remap = false, require = 0)
    public ItemStack cocoon$writeItemStack(ItemStack originItemStack, ItemStack itemStack, boolean flag) {
        return ItemHelper.wrapStackIfNeeded(itemStack);
    }
}
