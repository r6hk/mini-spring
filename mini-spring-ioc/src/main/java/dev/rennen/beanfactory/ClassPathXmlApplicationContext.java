package dev.rennen.beanfactory;

import dev.rennen.beans.define.ClassPathXmlResource;
import dev.rennen.beans.define.Resource;
import dev.rennen.beans.define.XmlBeanDefinitionReader;
import dev.rennen.exception.BeansException;
import lombok.extern.slf4j.Slf4j;
/**
 * @author rennen.dev
 * @since 2024/12/27 16:48
 */
@Slf4j
public class ClassPathXmlApplicationContext implements BeanFactory {
    SimpleBeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) throws BeansException {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
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

}


