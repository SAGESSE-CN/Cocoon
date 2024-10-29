package net.cocoonmc.runtime;

import com.mojang.serialization.Codec;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.Tag;
import net.cocoonmc.core.nbt.TagOps;

public interface ICodecFactory {

    Codec<BlockPos> getBlockPos();

    Codec<CompoundTag> getCompoundTag();

    Codec<ItemStack> getItemStack();

    TagOps<Tag> getTagOps();
}
