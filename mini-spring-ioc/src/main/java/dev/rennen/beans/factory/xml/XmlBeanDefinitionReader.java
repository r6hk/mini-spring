package dev.rennen.beans.factory.xml;

import dev.rennen.beans.define.Resource;
import dev.rennen.beans.factory.config.BeanDefinition;
import dev.rennen.beans.factory.config.ConstructorArgumentValue;
import dev.rennen.beans.factory.config.ConstructorArgumentValues;
import dev.rennen.beans.factory.AbstractBeanFactory;
import dev.rennen.beans.inject.PropertyValue;
import dev.rennen.beans.inject.PropertyValues;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:55
 */
public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            //处理属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (StringUtils.isNotBlank(pValue)) {
                    pV = pValue;
                } else if (StringUtils.isNotBlank(pRef)) {
                    pV = pRef;
                    isRef = true;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            //处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            this.beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
