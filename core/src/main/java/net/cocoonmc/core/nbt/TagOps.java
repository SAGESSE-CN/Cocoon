package net.cocoonmc.core.nbt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public interface TagOps<T extends Tag> {

    <A> DataResult<A> decode(final Codec<A> codec, final T input);

    <A> DataResult<T> encode(final Codec<A> codec, final A input);
}