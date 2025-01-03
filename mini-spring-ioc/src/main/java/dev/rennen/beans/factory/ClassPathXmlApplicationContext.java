package dev.rennen.beans.factory;

import dev.rennen.beans.define.ClassPathXmlResource;
import dev.rennen.beans.define.Resource;
import dev.rennen.beans.factory.config.BeanDefinition;
import dev.rennen.beans.factory.process.BeanFactoryPostProcessor;
import dev.rennen.beans.factory.process.BeanPostProcessor;
import dev.rennen.beans.factory.support.ConfigurableListableBeanFactory;
import dev.rennen.beans.factory.xml.XmlBeanDefinitionReader;
import dev.rennen.event.ContextRefreshedEvent;
import dev.rennen.event.core.ApplicationEvent;
import dev.rennen.event.core.ApplicationEventPublisher;
import dev.rennen.event.core.ApplicationListener;
import dev.rennen.event.core.SimpleApplicationEventPublisher;
import dev.rennen.exception.BeansException;
import dev.rennen.exception.CreateBeanInstanceErrorException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
/**
 * @author rennen.dev
 * @since 2024/12/27 16:48
 */
@Slf4j
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    DefaultListableBeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(@NonNull String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(@NonNull String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            refresh();
        }
    }

    @Override
    public
    void registerListeners() {
        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            Object bean = null;
            bean = getBean(bdName);
            if (bean instanceof ApplicationListener) {
                this.getApplicationEventPublisher().addListener((ApplicationListener<?>) bean);
            }
        }

    }

    @Override
    public
    void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public
    void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            BeanDefinition bd = this.beanFactory.getBeanDefinition(bdName);
            String clzName = bd.getClassName();
            Class<?> clz = null;
            try {
                clz = Class.forName(clzName);
            } catch (ClassNotFoundException e) {
                throw new CreateBeanInstanceErrorException("create bean instance error");
            }
            if (BeanFactoryPostProcessor.class.isAssignableFrom(clz)) {
                try {
                    getBeanFactoryPostProcessors().add((BeanFactoryPostProcessor) clz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new CreateBeanInstanceErrorException("create bean instance error");
                }
            }
        }
        for (BeanFactoryPostProcessor processor : getBeanFactoryPostProcessors()) {
            try {
                processor.postProcessBeanFactory(bf);
            } catch (BeansException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public
    void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        System.out.println("try to registerBeanPostProcessors");
        String[] bdNames = this.beanFactory.getBeanDefinitionNames();
        for (String bdName : bdNames) {
            BeanDefinition bd = this.beanFactory.getBeanDefinition(bdName);
            String clzName = bd.getClassName();
            Class<?> clz = null;
            try {
                clz = Class.forName(clzName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            if (BeanPostProcessor.class.isAssignableFrom(clz)) {
                System.out.println(" registerBeanPostProcessors : " + clzName);
                try {
                    //this.beanFactory.addBeanPostProcessor((BeanPostProcessor) clz.newInstance());
                    this.beanFactory.addBeanPostProcessor((BeanPostProcessor)(this.beanFactory.getBean(bdName)));
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public
    void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void addListener(ApplicationListener<?> listener) {
        this.getApplicationEventPublisher().addListener(listener);

    }

    @Override
    public
    void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));

    }

    @Override
    public void publishEvent(ApplicationEvent<?> event) {
        this.getApplicationEventPublisher().publishEvent(event);

    }



    @Override
    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }
}


