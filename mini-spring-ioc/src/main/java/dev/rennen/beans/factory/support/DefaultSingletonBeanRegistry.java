package dev.rennen.beans.factory.support;

import dev.rennen.beans.factory.config.SingletonBeanRegistry;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rennen.dev
 * @since 2024/12/30 16:39
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 容器中存放所有 bean 名称的列表
    protected final List<String> beanNames = new ArrayList<>();

    // 容器中存放所有 bean 实例的 Map
    protected final Map<String, Object> singletons = new ConcurrentHashMap<>(256);

    @Override
    public void registerSingleton(@NonNull String beanName, @NonNull Object singletonObject) {
        synchronized (this.beanNames) {
            this.beanNames.add(beanName);
            this.singletons.put(beanName, singletonObject);
        }
    }

    @Override
    public Object getSingleton(@NonNull String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(@NonNull String beanName) {
        return this.singletons.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        synchronized (this.beanNames) {
            return this.beanNames.toArray(new String[0]);
        }
    }

    protected void removeSingleton(@NonNull String beanName) {
        synchronized (this.beanNames) {
            this.beanNames.remove(beanName);
            this.singletons.remove(beanName);
        }
    }
}
