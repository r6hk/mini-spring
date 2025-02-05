# mini-spring 项目说明

## 项目简介
一个轻量级的开源框架，旨在模拟 **Spring 框架** 的核心功能，帮助深入理解其内部机制。

## 核心功能
- **实现 IoC 容器**，支持基于 XML 的 Bean 定义与依赖注入，具备单例与原型模式管理，支持构造方法注入、属性注入、`@Autowired` 注解注入。
- 提供简单的 **AOP 支持**，方便实现横切关注点如日志、事务管理等。
- 实现基本的 DispatcherServlet，支持注解 `@RequestMapping` 和自动扫描 Controller，便于**通过 MVC 模式快速开发 Web 应用**。
- **封装基础的数据库访问逻辑**，简化 JDBC 编程，支持数据源配置与自动注入，实现简易的线程安全数据库连接池。
- **内置简单的事件机制**，支持事件发布与监听，提升应用的解耦性。

## 与 Spring 框架的对比
| 特性          | Minis (mini-Spring)                | Spring Framework                   |
|----------------------|-------------------------------------------|------------------------------------------|
| **IoC 容器**        | 基于简单 XML 配置，支持基本依赖注入         | 完善的 XML、注解、Java Config 支持      |
| **AOP**             | 支持基础 AOP 功能，手动配置切面               | 强大的 AOP 支持，基于 AspectJ 与代理模式  |
| **MVC**             | 简化版 DispatcherServlet，支持注解映射       | 完整的 Spring MVC，丰富的视图解析与数据绑定 |
| **数据库支持**       | 内置简单 JdbcTemplate，支持基本 CRUD 操作   | 集成 JPA、MyBatis 等，支持事务管理与声明式 SQL |
| **依赖注入方式**      | 基于 XML，部分支持 `@Autowired` 注解          | 完整支持注解、自动扫描、条件注入等特性      |
| **扩展性**            | 适合学习与定制化开发，代码结构简单明了       | 企业级框架，功能全面，适合复杂项目           |

## 开发环境
- IDEA 2024.3
- JDK 21
- Maven 3.9
- Servlet 6（配套 Tomcat 10）
- 自带 H2 Database 方便开发

## 快速开始
### 1. 克隆项目
```bash
git clone https://github.com/your-repo/minis.git
cd minis
```

### 2. 配置 Bean
在 `applicationContext.xml` 中定义 Bean：
```xml
<bean id="aservice" class="com.example.AServiceImpl">
    <property name="name" value="Mini Spring"/>
</bean>
```

### 3. 使用 IoC 容器
```java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
AService aService = (AService) context.getBean("aservice");
aService.doSomething();
```

> [!TIP]
> 可于以下位置查看更多使用示例：
> - IoC 相关功能测试：`dev.rennen.tests.ClassPathXmlApplicationContextTest`
> - AOP 相关功能测试：`dev.rennen.tests.AopProxyTest`
> - MVC 相关功能测试：`dev.rennen.webmvc.test.controller.HelloWorldController`
> - JDBC 相关功能测试：`dev.rennen.jdbc.test.JdbcTest`
