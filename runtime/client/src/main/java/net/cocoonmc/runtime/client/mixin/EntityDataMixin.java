package net.cocoonmc.runtime.client.mixin;

import net.cocoonmc.runtime.client.helper.EntityHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SynchedEntityData.class)
public class EntityDataMixin {

    @Inject(method = "defineId", at = @At("RETURN"))
    private static void cocoon$defineId(Class<? extends Entity> class_, EntityDataSerializer<?> arg, CallbackInfoReturnable<EntityDataAccessor<?>> cir) {
        EntityDataAccessor<?> accessor = cir.getReturnValue();
        EntityHelper.registerData(class_, accessor::getId, () -> EntityDataSerializers.getSerializedId(accessor.getSerializer()));
    }
}
