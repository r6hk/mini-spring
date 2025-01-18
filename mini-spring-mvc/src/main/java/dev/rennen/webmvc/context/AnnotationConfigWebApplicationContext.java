package dev.rennen.webmvc.context;

import dev.rennen.beans.factory.AbstractApplicationContext;
import dev.rennen.beans.factory.DefaultListableBeanFactory;
import dev.rennen.beans.factory.config.BeanDefinition;
import dev.rennen.beans.factory.process.BeanFactoryPostProcessor;
import dev.rennen.beans.factory.process.impl.AutowiredAnnotationBeanPostProcessor;
import dev.rennen.beans.factory.support.ConfigurableListableBeanFactory;
import dev.rennen.event.core.ApplicationListener;
import dev.rennen.event.core.SimpleApplicationEventPublisher;
import dev.rennen.event.eventobject.ContextRefreshedEvent;
import dev.rennen.event.listener.MyListener;
import dev.rennen.webmvc.exception.ParseXmlException;
import dev.rennen.webmvc.web.XmlScanComponentHelper;
import jakarta.servlet.ServletContext;

import javax.annotation.Nullable;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 2025/1/7 16:27
 *
 * @author rennen.dev
 */
public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {

    private WebApplicationContext parentApplicationContext;

    private ServletContext servletContext;

    private DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AnnotationConfigWebApplicationContext(String configFileName) {
        // FIXME: NPE
        this(configFileName, null);
    }

    public AnnotationConfigWebApplicationContext(String configFileName, @Nullable WebApplicationContext parent) {
        this.parentApplicationContext = parent;
        this.servletContext = parent.getServletContext();
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(configFileName);
        } catch (MalformedURLException e) {
            throw new ParseXmlException("xml path is not correct", e);
        }

        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        this.beanFactory = new DefaultListableBeanFactory();
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
        loadBeanDefinitions(controllerNames);

        try {
            refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        //将以.分隔的包名换成以/分隔的uri
        try {
            URL url = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"));
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(uri);
        //处理对应的文件目录
        for (File file : Objects.requireNonNull(dir.listFiles())) { //目录下的文件或者子目录
            if (file.isDirectory()) { //对子目录递归扫描
                scanPackage(packageName + "." + file.getName());
            } else { //类文件
                String controllerName = packageName + "."
                        + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    public void loadBeanDefinitions(List<String> controllerNames) {
        for (String controller : controllerNames) {
            BeanDefinition beanDefinition = new BeanDefinition(controller, controller);
            this.beanFactory.registerBeanDefinition(controller, beanDefinition);
        }
    }

    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void addApplicationListener(ApplicationListener<?> listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
        // do nothing
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        // do nothing
    }

    @Override
    public void registerListeners() {
        ApplicationListener<ContextRefreshedEvent> listener = new MyListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void initApplicationEventPublisher() {
        SimpleApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public void publishEvent(Object event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public Object getBean(String beanName) {
        Object result = super.getBean(beanName);
        if (result == null) {
            result = this.parentApplicationContext.getBean(beanName);
        }
        return result;
    }
}

