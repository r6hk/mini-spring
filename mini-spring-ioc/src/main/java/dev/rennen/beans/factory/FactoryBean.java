package dev.rennen.beans.factory;

/**
 * <br/> 2025/2/4 21:22
 *
 * @author rennen.dev
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}