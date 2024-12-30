package dev.rennen.beans.inject;

import java.util.*;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:50
 */
public class ArgumentValues {

    private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>(0);
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    private void addArgumentValue(int index, ArgumentValue value) {
        indexedArgumentValues.put(index, value);
    }

    public boolean hasIndexedArgumentValue(int index) {
        return indexedArgumentValues.containsKey(index);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return indexedArgumentValues.get(index);
    }

    public void addGenericArgumentValue(Object value, String type) {
        genericArgumentValues.add(new ArgumentValue(value, type));
    }

    private void addGenericArgumentValue(ArgumentValue newValue) {
        if (newValue.getName() != null) {
            this.genericArgumentValues
                    .removeIf(currentValue -> newValue.getName().equals(currentValue.getName()));
        }
        this.genericArgumentValues.add(newValue);
    }

    public ArgumentValue getGenericArgumentValue(String requiredName) {
        for (ArgumentValue valueHolder : this.genericArgumentValues) {
            if (valueHolder.getName() != null && (!valueHolder.getName().equals(requiredName))) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }
}
