package dev.rennen.beanfactory;

import dev.rennen.beans.ClassPathXmlResource;
import dev.rennen.beans.Resource;
import dev.rennen.beans.XmlBeanDefinitionReader;
import dev.rennen.exception.BeansException;
import org.dom4j.DocumentException;
import dev.rennen.beans.BeanDefinition;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rennen.dev
 * @since 2024/12/27 16:48
 */
@Slf4j
public class ClassPathXmlApplicationContext implements BeanFactory {
    BeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) throws BeansException {
        Resource resource = new ClassPathXmlResource(fileName);
        BeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}


