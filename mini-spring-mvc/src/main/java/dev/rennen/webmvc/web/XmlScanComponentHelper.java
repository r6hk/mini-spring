package dev.rennen.webmvc.web;

import dev.rennen.webmvc.exception.ParseXmlException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 2025/1/5 19:16
 *
 * @author rennen.dev
 */
public class XmlScanComponentHelper {
    public static List<String> getNodeValue(URL xmlPath) {
        List<String> packages = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(xmlPath); //加载配置文件
        } catch (DocumentException e) {
            throw new ParseXmlException("parse '<component-scan>' xml element error", e);
        }
        Element root = document.getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) { //得到XML中所有的base-package节点
            Element element = it.next();
            packages.add(element.attributeValue("base-package"));
        }
        return packages;
    }
}
