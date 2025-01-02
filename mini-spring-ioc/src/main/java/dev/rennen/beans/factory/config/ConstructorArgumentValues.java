package dev.rennen.beans.factory.config;

import java.util.*;

/**
 * ConstructorArgumentValue 的集合封装形式
 *
 * @author rennen.dev
 * @since 2024/12/30 17:50
 */
public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.constructorArgumentValueList.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.constructorArgumentValueList.get(index);
    }

    public int getArgumentCount() {
        return (this.constructorArgumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.constructorArgumentValueList.isEmpty());
    }
}