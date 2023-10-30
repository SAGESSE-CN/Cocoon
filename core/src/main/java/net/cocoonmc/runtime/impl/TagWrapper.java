package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.nbt.ByteArrayTag;
import net.cocoonmc.core.nbt.ByteTag;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.DoubleTag;
import net.cocoonmc.core.nbt.FloatTag;
import net.cocoonmc.core.nbt.IntArrayTag;
import net.cocoonmc.core.nbt.IntTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.nbt.LongArrayTag;
import net.cocoonmc.core.nbt.LongTag;
import net.cocoonmc.core.nbt.ShortTag;
import net.cocoonmc.core.nbt.StringTag;
import net.cocoonmc.core.nbt.Tag;

public class TagWrapper {

    protected abstract static class WrappedTag implements Tag {

        private final Object handle;

        protected WrappedTag(Object handle) {
            this.handle = handle;
        }

        @Override
        public Object getHandle() {
            return handle;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Tag) {
                return handle.equals(((Tag) obj).getHandle());
            }
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return handle.toString();
        }

        @Override
        public int hashCode() {
            return handle.hashCode();
        }
    }

    protected abstract static class WrappedByteTag extends WrappedTag implements ByteTag {

        protected WrappedByteTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedShortTag extends WrappedTag implements ShortTag {
        protected WrappedShortTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedIntTag extends WrappedTag implements IntTag {
        protected WrappedIntTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedLongTag extends WrappedTag implements LongTag {
        protected WrappedLongTag(Object handle) {
            super(handle);
        }
    }


    protected abstract static class WrappedFloatTag extends WrappedTag implements FloatTag {
        protected WrappedFloatTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedDoubleTag extends WrappedTag implements DoubleTag {
        protected WrappedDoubleTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedStringTag extends WrappedTag implements StringTag {
        protected WrappedStringTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedByteArrayTag extends WrappedTag implements ByteArrayTag {
        protected WrappedByteArrayTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedIntArrayTag extends WrappedTag implements IntArrayTag {
        protected WrappedIntArrayTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedLongArrayTag extends WrappedTag implements LongArrayTag {
        protected WrappedLongArrayTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedCompoundTag extends WrappedTag implements CompoundTag {
        protected WrappedCompoundTag(Object handle) {
            super(handle);
        }
    }

    protected abstract static class WrappedListTag extends WrappedTag implements ListTag {
        protected WrappedListTag(Object handle) {
            super(handle);
        }
    }
}
