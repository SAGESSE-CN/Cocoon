package net.cocoonmc.runtime.client.mixin;

import net.cocoonmc.runtime.client.api.Available;
import net.cocoonmc.runtime.client.api.CocoonLevelChunk;
import net.cocoonmc.runtime.client.helper.ItemHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Available("[1.19, )")
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @ModifyVariable(method = "setServerVerifiedBlockState", at = @At("HEAD"), argsOnly = true)
    private BlockState cocoon$setServerVerifiedBlockState(BlockState blockState, BlockPos pos, BlockState arg2, int i) {
        ClientLevel level = ClientLevel.class.cast(this);
        ChunkAccess chunk = level.getChunk(pos);
        if (!(chunk instanceof CocoonLevelChunk)) {
            return blockState;
        }
        CocoonLevelChunk originalChunk = (CocoonLevelChunk) chunk;
        BlockState originalState = originalChunk.cocoon$getOriginalBlockState(pos);
        if (originalState == null) {
            return blockState;
        }
        // because we can't get the real block state from this event,
        // so we will keep current block state when the block type not changes.
        if (blockState.getBlock().equals(originalState.getBlock())) {
            originalChunk.cocoon$setOriginalBlockState(pos, blockState);
            return level.getBlockState(pos);
        }
        // reset all data
        originalChunk.cocoon$setOriginalBlockState(pos, null);
        return blockState;
    }

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void cocoon$handleLogin(CallbackInfo ci) {
        ItemHelper.setEnableRedirect(false);
    }
}
