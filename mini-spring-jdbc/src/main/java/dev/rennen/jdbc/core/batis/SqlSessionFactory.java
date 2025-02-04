package dev.rennen.jdbc.core.batis;

/**
 * <br/> 2025/2/4 16:59
 *
 * @author rennen.dev
 */
public interface SqlSessionFactory {
    SqlSession openSession();
    MapperNode getMapperNode(String name);
}