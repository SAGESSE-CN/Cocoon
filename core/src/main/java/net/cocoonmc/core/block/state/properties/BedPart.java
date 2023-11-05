package net.cocoonmc.core.block.state.properties;

public enum BedPart {

    HEAD("head"),
    FOOT("foot");

    private final String name;

    BedPart(String string2) {
        this.name = string2;
    }

    public String getName() {
        return name;
    }
}
