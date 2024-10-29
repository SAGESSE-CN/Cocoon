package net.cocoonmc.runtime.impl;

import net.cocoonmc.core.component.DataComponentMap;

public interface ItemStackAccessor {

    void setCount(int count);

    DataComponentMap getComponents();

    void setComponents(DataComponentMap components);
}
