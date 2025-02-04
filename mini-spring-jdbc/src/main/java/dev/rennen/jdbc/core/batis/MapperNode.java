package dev.rennen.jdbc.core.batis;

import lombok.Getter;
import lombok.Setter;

/**
 * 存放系统中 SQL 语句的定义 <br/> 2025/2/4 16:47
 *
 * @author rennen.dev
 */
@Getter @Setter
public class MapperNode {
    String namespace;
    String id;
    String parameterType;
    String resultType;
    String sql;
    String parameter;

    public String toString() {
        return this.namespace + "." + this.id + " : " + this.sql;
    }
}