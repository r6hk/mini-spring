package dev.rennen.jdbc.core.batis;

import dev.rennen.beans.factory.annotation.Autowired;
import dev.rennen.jdbc.core.JdbcTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <br/> 2025/2/4 17:02
 *
 * @author rennen.dev
 */

@Slf4j
public class DefaultSqlSessionFactory implements SqlSessionFactory{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Setter
    @Getter
    String mapperLocations;

    @Getter
    Map<String,MapperNode> mapperNodeMap = new HashMap<>();

    public DefaultSqlSessionFactory(String mapperLocations) {
        this.mapperLocations = mapperLocations;
        init();
    }

    public void init() {
        scanLocation(this.mapperLocations);
    }

    private void scanLocation(String location) {
        String sLocationPath = this.getClass().getClassLoader().getResource("").getPath()+location;
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                // 递归扫描
                scanLocation(location+"/"+file.getName());
            }else{
                buildMapperNodes(location+"/"+file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath) {
        System.out.println(filePath);
        SAXReader saxReader=new SAXReader();
        URL xmlPath=this.getClass().getClassLoader().getResource(filePath);
        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement=document.getRootElement();

            String namespace = rootElement.attributeValue("namespace");

            Iterator<Element> nodes = rootElement.elementIterator();;
            while (nodes.hasNext()) {
                Element node = nodes.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode selectNode = new MapperNode();
                selectNode.setNamespace(namespace);
                selectNode.setId(id);
                selectNode.setParameterType(parameterType);
                selectNode.setResultType(resultType);
                selectNode.setSql(sql);
                selectNode.setParameter("");
                // 加入到集合中
                this.mapperNodeMap.put(namespace + "." + id, selectNode);
            }
        } catch (Exception ex) {
            log.error("解析文件出错", ex);
        }
        return this.mapperNodeMap;
    }

    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

    @Override
    public SqlSession openSession() {
        SqlSession newSqlSession = new DefaultSqlSession();
        newSqlSession.setJdbcTemplate(jdbcTemplate);
        newSqlSession.setSqlSessionFactory(this);

        return newSqlSession;
    }
}