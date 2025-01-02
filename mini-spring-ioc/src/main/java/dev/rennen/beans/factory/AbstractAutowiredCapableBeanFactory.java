package dev.rennen.beans.factory;

import dev.rennen.beans.factory.process.BeanPostProcessor;
import dev.rennen.beans.factory.process.impl.AutowiredAnnotationBeanPostProcessor;
import dev.rennen.beans.factory.support.AutowiredCapableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rennen.dev
 * @since 2025/1/2 11:13
 */
public class AbstractAutowiredCapableBeanFactory extends AbstractBeanFactory implements AutowiredCapableBeanFactory {
    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor postProcessor : getBeanPostProcessors()) {
            postProcessor.setBeanFactory(this);
            result = postProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                break;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                break;
            }
        }
        return result;
    }
}
