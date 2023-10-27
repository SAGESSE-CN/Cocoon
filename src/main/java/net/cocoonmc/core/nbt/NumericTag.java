package net.cocoonmc.core.nbt;

@SuppressWarnings("unused")
public interface NumericTag extends Tag {

    default long getAsLong() {
        return getAsNumber().longValue();
    }

    default int getAsInt() {
        return getAsNumber().intValue();
    }

    default short getAsShort() {
        return getAsNumber().shortValue();
    }

    default byte getAsByte() {
        return getAsNumber().byteValue();
    }

    default double getAsDouble() {
        return getAsNumber().doubleValue();
    }

    default float getAsFloat() {
        return getAsNumber().floatValue();
    }

    Number getAsNumber();
}
