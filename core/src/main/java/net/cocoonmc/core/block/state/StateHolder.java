package net.cocoonmc.core.block.state;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.cocoonmc.core.block.state.properties.Property;
import net.cocoonmc.core.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StateHolder<O, S> {

    protected final O owner;
    private final ImmutableMap<Property<?>, Comparable<?>> values;
    private Table<Property<?>, Comparable<?>, S> neighbours;

    protected StateHolder(O owner, ImmutableMap<Property<?>, Comparable<?>> values) {
        this.owner = owner;
        this.values = values;
    }

    public <T extends Comparable<T>> T getValue(Property<T> property) {
        Comparable<?> entry = values.get(property);
        if (entry == null) {
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + owner);
        }
        return property.getValueClass().cast(entry);
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>, V extends T> S setValue(Property<T> property, V value) {
        Comparable<?> entry = values.get(property);
        if (entry == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.owner);
        }
        if (entry.equals(value)) {
            return (S) this;
        }
        S object = neighbours.get(property, value);
        if (object == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this.owner + ", it is not an allowed value");
        }
        return object;
    }

    public CompoundTag serialize() {
        CompoundTag tag = CompoundTag.newInstance();
        values.forEach((property, value) -> tag.putString(property.getName(), getValueName(property, value)));
        return tag;
    }

    @SuppressWarnings("unchecked")
    public S deserialize(CompoundTag tag) {
        StateHolder<O, S> state = this;
        for (Property<?> property : values.keySet()) {
            String name = property.getName();
            if (tag.contains(name, 8)) {
                state = setValueByName(state, property, tag.getString(name));
            }
        }
        return (S) state;
    }


    public ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(owner);
        if (!this.getValues().isEmpty()) {
            stringBuilder.append('[');
            stringBuilder.append(getValues().entrySet().stream().map(this::getEntryName).collect(Collectors.joining(",")));
            stringBuilder.append(']');
        }
        return stringBuilder.toString();
    }

    public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> allStates) {
        if (neighbours != null) {
            throw new IllegalStateException();
        }
        HashBasedTable<Property<?>, Comparable<?>, S> table = HashBasedTable.create();
        values.forEach((property, value) -> property.getPossibleValues().forEach(possibleValue -> {
            if (!possibleValue.equals(value)) {
                table.put(property, possibleValue, allStates.get(makeNeighbourValues(property, possibleValue)));
            }
        }));
        neighbours = table.isEmpty() ? table : ArrayTable.create(table);
    }

    private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> property, Comparable<?> value) {
        HashMap<Property<?>, Comparable<?>> map = Maps.newHashMap(values);
        map.put(property, value);
        return map;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> StateHolder<O, S> setValueByName(StateHolder<O, S> state, Property<T> property, String valueName) {
        Optional<T> value = property.getValue(valueName);
        if (value.isPresent()) {
            return (StateHolder<O, S>) setValue(property, value.get());
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> String getValueName(Property<T> property, Comparable<?> value) {
        return property.getName((T) value);
    }

    private String getEntryName(Map.Entry<Property<?>, Comparable<?>> entry) {
        if (entry == null) {
            return "<NULL>";
        }
        Property<?> property = entry.getKey();
        return property.getName() + "=" + getValueName(property, entry.getValue());
    }
}
