package net.cocoonmc.runtime.v1_21_R1;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.cocoonmc.core.nbt.TagOps;
import net.cocoonmc.runtime.ICodecFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class CodecFactory implements ICodecFactory {

    @Override
    public Codec<net.cocoonmc.core.BlockPos> getBlockPos() {
        return BlockPos.CODEC.xmap(TransformFactory::convertToCocoon, TransformFactory::convertToVanilla);
    }

    @Override
    public Codec<net.cocoonmc.core.nbt.CompoundTag> getCompoundTag() {
        return CompoundTag.CODEC.xmap(TagFactory::wrap, TagFactory::unwrap);
    }

    @Override
    public Codec<net.cocoonmc.core.item.ItemStack> getItemStack() {
        return ItemStack.CODEC.xmap(TransformFactory::convertToCocoon, TransformFactory::convertToVanilla);
    }

    @Override
    public TagOps<net.cocoonmc.core.nbt.Tag> getTagOps() {
        return TagOpsImpl.INSTANCE;
    }

    public static class TagOpsImpl implements TagOps<net.cocoonmc.core.nbt.Tag> {
        public static final TagOpsImpl INSTANCE = new TagOpsImpl();

        private final NbtOps ops = NbtOps.INSTANCE;

        @Override
        public <A> DataResult<A> decode(Codec<A> codec, net.cocoonmc.core.nbt.Tag input) {
            DataResult<Pair<A, Tag>> result = codec.decode(ops, TagFactory.unwrap(input));
            return result.map(Pair::getFirst);
        }

        @Override
        public <A> DataResult<net.cocoonmc.core.nbt.Tag> encode(Codec<A> codec, A input) {
            DataResult<Tag> result = codec.encodeStart(ops, input);
            return result.map(TagFactory::wrap);
        }
    }
}
