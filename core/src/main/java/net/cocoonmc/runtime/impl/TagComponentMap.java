package net.cocoonmc.runtime.impl;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.component.DataComponentMap;
import net.cocoonmc.core.component.DataComponentType;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.Tag;
import net.cocoonmc.core.nbt.TagOps;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class TagComponentMap implements DataComponentMap {

    protected CompoundTag tag;

    private final TagOps<Tag> ops = Cocoon.API.CODEC.getTagOps();

    public TagComponentMap(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public boolean has(DataComponentType<?> componentType) {
        CompoundTag tag = getTag();
        if (tag != null) {
            return tag.contains(componentType.getTagName());
        }
        return false;
    }

    @Override
    public <T> void set(DataComponentType<? super T> componentType, @Nullable T object) {
        if (object != null) {
            CompoundTag tag = getOrCreateTag();
            ops.encode(componentType.getCodec(), object).get().ifLeft(it -> {
                // we need to merge new value into the item.
                tag.put(componentType.getTagName(), it);
                tagDidChange();
            });
        } else {
            remove(componentType);
        }
    }

    @Nullable
    @Override
    public <T> T get(DataComponentType<? extends T> componentType) {
        CompoundTag tag = getTag();
        if (tag != null && tag.contains(componentType.getTagName())) {
            Optional<? extends T> value = ops.decode(componentType.getCodec(), tag.get(componentType.getTagName())).get().left();
            if (value.isPresent()) {
                return value.get();
            }
        }
        return null;
    }

    @Override
    public void remove(DataComponentType<?> componentType) {
        CompoundTag tag = getTag();
        if (tag != null) {
            tag.remove(componentType.getTagName());
            tagDidChange();
        }
    }

    @Override
    public DataComponentMap copy() {
        if (tag != null) {
            return new TagComponentMap(tag.copy());
        }
        return new TagComponentMap(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagComponentMap)) return false;
        return Objects.equals(tag, ((TagComponentMap) o).tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    public void tagDidChange() {
    }

    public CompoundTag getTag() {
        return tag;
    }

    public CompoundTag getOrCreateTag() {
        if (tag != null) {
            return tag;
        }
        tag = CompoundTag.newInstance();
        tagDidChange();
        return tag;
    }
}
