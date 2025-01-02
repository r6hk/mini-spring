package dev.rennen.beans.factory;

import dev.rennen.beans.define.ClassPathXmlResource;
import dev.rennen.beans.define.Resource;
import dev.rennen.beans.factory.support.SimpleBeanFactory;
import dev.rennen.beans.factory.xml.XmlBeanDefinitionReader;
import dev.rennen.event.ApplicationEvent;
import dev.rennen.event.ApplicationEventPublisher;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
/**
 * @author rennen.dev
 * @since 2024/12/27 16:48
 */
@Slf4j
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    SimpleBeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(@NonNull String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(@NonNull String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            this.beanFactory.refresh();
        }
    }

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}


