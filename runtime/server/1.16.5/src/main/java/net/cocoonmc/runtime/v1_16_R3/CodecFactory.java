package net.cocoonmc.runtime.v1_16_R3;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.cocoonmc.core.nbt.TagOps;
import net.cocoonmc.runtime.ICodecFactory;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.DynamicOpsNBT;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.ItemStack;

public class CodecFactory implements ICodecFactory {

    @Override
    public Codec<net.cocoonmc.core.BlockPos> getBlockPos() {
        return BlockPosition.a.xmap(TransformFactory::convertToCocoon, TransformFactory::convertToVanilla);
    }

    @Override
    public Codec<net.cocoonmc.core.nbt.CompoundTag> getCompoundTag() {
        return NBTTagCompound.a.xmap(TagFactory::wrap, TagFactory::unwrap);
    }

    @Override
    public Codec<net.cocoonmc.core.item.ItemStack> getItemStack() {
        return ItemStack.a.xmap(TransformFactory::convertToCocoon, TransformFactory::convertToVanilla);
    }

    @Override
    public TagOps<net.cocoonmc.core.nbt.Tag> getTagOps() {
        return TagOpsImpl.INSTANCE;
    }

    public static class TagOpsImpl implements TagOps<net.cocoonmc.core.nbt.Tag> {
        public static final TagOpsImpl INSTANCE = new TagOpsImpl();

        private final DynamicOpsNBT ops = DynamicOpsNBT.a;

        @Override
        public <A> DataResult<A> decode(Codec<A> codec, net.cocoonmc.core.nbt.Tag input) {
            DataResult<Pair<A, NBTBase>> result = codec.decode(ops, TagFactory.unwrap(input));
            return result.map(Pair::getFirst);
        }

        @Override
        public <A> DataResult<net.cocoonmc.core.nbt.Tag> encode(Codec<A> codec, A input) {
            DataResult<NBTBase> result = codec.encodeStart(ops, input);
            return result.map(TagFactory::wrap);
        }
    }
}
