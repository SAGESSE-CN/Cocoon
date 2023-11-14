package net.cocoonmc.runtime.client.mixin;

import net.cocoonmc.runtime.client.api.Available;
import net.cocoonmc.runtime.client.helper.EntityHelper;
import net.cocoonmc.runtime.client.helper.RegistryHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Available("[1.16, )")
@Mixin(ClientboundAddEntityPacket.class)
public class ClientLevelEntityMixin {

    @Unique
    private Supplier<EntityType<?>> cocoon$type;

    @Inject(method = "getType", at = @At("RETURN"), cancellable = true)
    private void cocoon$getType(CallbackInfoReturnable<EntityType<?>> cir) {
        if (cocoon$type != null) {
            if (cocoon$type.get() != null) {
                cir.setReturnValue(cocoon$type.get());
            }
            return;
        }
        ClientboundAddEntityPacket packet = ClientboundAddEntityPacket.class.cast(this);
        CompoundTag tag = EntityHelper.PENDING_NEW_ENTITIES.remove(packet.getId());
        if (tag == null || !tag.contains("type")) {
            cocoon$type = () -> null;
            return;
        }
        ResourceLocation rl = new ResourceLocation(tag.getString("type"));
        EntityType<?> type = RegistryHelper.ENTITY_TYPES.getOptional(rl).orElse(null);
        cocoon$type = () -> type;
        if (type != null) {
            cir.setReturnValue(type);
        }
    }
}
