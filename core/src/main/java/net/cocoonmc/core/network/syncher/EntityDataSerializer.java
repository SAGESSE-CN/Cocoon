package net.cocoonmc.core.network.syncher;

import net.cocoonmc.core.network.FriendlyByteBuf;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface EntityDataSerializer<T> {

    void write(FriendlyByteBuf buf, T value);

    T read(FriendlyByteBuf buf);


    static <T> EntityDataSerializer<T> simple(final BiConsumer<FriendlyByteBuf, T> writer, final Function<FriendlyByteBuf, T> reader) {
        return new EntityDataSerializer<>() {
            @Override
            public void write(FriendlyByteBuf buf, T value) {
                writer.accept(buf, value);
            }

            @Override
            public T read(FriendlyByteBuf buf) {
                return reader.apply(buf);
            }
        };
    }

    static <T> EntityDataSerializer<Optional<T>> optional(final BiConsumer<FriendlyByteBuf, T> writer, final Function<FriendlyByteBuf, T> reader) {
        return new EntityDataSerializer<>() {
            @Override
            public void write(FriendlyByteBuf buf, Optional<T> value) {
                if (value.isPresent()) {
                    buf.writeBoolean(true);
                    writer.accept(buf, value.get());
                } else {
                    buf.writeBoolean(false);
                }
            }

            @Override
            public Optional<T> read(FriendlyByteBuf buf) {
                if (buf.readBoolean()) {
                    return Optional.of(reader.apply(buf));
                }
                return Optional.empty();
            }
        };
    }

    static <T extends Enum<T>> EntityDataSerializer<T> simpleEnum(Class<T> clazz) {
        return simple(FriendlyByteBuf::writeEnum, friendlyByteBuf -> friendlyByteBuf.readEnum(clazz));
    }
}
