package net.cocoonmc.runtime.client.mixin;

import com.mojang.datafixers.util.Pair;
import net.cocoonmc.runtime.client.api.Available;
import net.cocoonmc.runtime.client.api.CocoonLevelChunk;
import net.cocoonmc.runtime.client.helper.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

@Available("[1.18, )")
@Mixin(LevelChunk.class)
public class ClientLevelChunkMixin implements CocoonLevelChunk {

    @Unique
    private HashMap<BlockPos, BlockState> cocoon$originalBlockStates;

    @Inject(method = "replaceWithPacketData", at = @At("RETURN"))
    public void cocoon$replaceWithPacketData(FriendlyByteBuf arg, CompoundTag arg2, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfo ci) {
        LevelChunk chunk = LevelChunk.class.cast(this);
        ArrayList<Runnable> pending = new ArrayList<>();
        ListTag tag = arg2.getList("__redirected_chunk__", 10);
        tag.forEach(it -> {
            CompoundTag blockTag = (CompoundTag) it;
            BlockPos pos = BlockHelper.getBlockPos(blockTag);
            Pair<BlockState, CompoundTag> pair = BlockHelper.getBlockFromTag(blockTag);
            if (pair == null) {
                return;
            }
            // replace the state.
            pending.add(() -> {
                BlockState oldState = chunk.getBlockState(pos);
                BlockState newState = pair.getFirst();
                CompoundTag newTag = pair.getSecond();
                cocoon$setOriginalBlockState(pos, oldState);
                chunk.setBlockState(pos, newState, false);
                if (newTag != null) {
                    BlockEntity blockEntity = chunk.getBlockEntity(pos);
                    if (blockEntity != null) {
                        blockEntity.load(newTag);
                    }
                }
            });
        });
        try {
            pending.forEach(Runnable::run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cocoon$setOriginalBlockState(BlockPos pos, BlockState blockState) {
        if (cocoon$originalBlockStates == null) {
            cocoon$originalBlockStates = new HashMap<>();
        }
        cocoon$originalBlockStates.put(pos, blockState);
    }

    @Override
    public BlockState cocoon$getOriginalBlockState(BlockPos pos) {
        if (cocoon$originalBlockStates != null) {
            return cocoon$originalBlockStates.get(pos);
        }
        return null;
    }
}
