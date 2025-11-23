# 图书管理系统后端

这是一个基于 Spring Boot 3.2.0 开发的前后端分离图书管理系统后端项目。

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: 数据持久化
- **Spring Security**: 安全框架（已配置CORS）
- **MySQL**: 数据库
- **Lombok**: 简化代码
- **Maven**: 项目构建工具

## 功能特性

- ✅ 用户管理（注册、登录、CRUD）
- ✅ 分类管理（CRUD）
- ✅ 图书管理（CRUD、搜索、分类筛选）
- ✅ 借阅管理（借阅、归还、查询）
- ✅ 统一异常处理
- ✅ 参数验证
- ✅ CORS 跨域支持
- ✅ 密码加密（BCrypt）

## 项目结构

```
src/main/java/com/library/
├── common/          # 通用类（Result等）
├── config/          # 配置类（Security、Web等）
├── controller/      # 控制器层
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── exception/       # 异常处理
├── repository/      # 数据访问层
└── service/         # 业务逻辑层
    └── impl/        # 服务实现类
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE library CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true
    username: root
    password: root
```

### 3. 运行项目

```bash
# 使用 Maven 运行
mvn spring-boot:run

# 或者先编译再运行
mvn clean package
java -jar target/library-backend-0.0.1-SNAPSHOT.jar
```

### 4. 访问接口

- 服务地址: `http://localhost:8080`
- 健康检查: `http://localhost:8080/api/health`
- API 文档: 查看 `API_DOCUMENTATION.md`

## API 接口

### 用户管理
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取用户详情
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 分类管理
- `POST /api/categories` - 添加分类
- `GET /api/categories` - 获取所有分类
- `GET /api/categories/{id}` - 获取分类详情
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类

### 图书管理
- `POST /api/books` - 添加图书
- `GET /api/books` - 获取所有图书（支持分类筛选和关键词搜索）
- `GET /api/books/{id}` - 获取图书详情
- `PUT /api/books/{id}` - 更新图书
- `DELETE /api/books/{id}` - 删除图书

### 借阅管理
- `POST /api/borrows` - 借阅图书
- `PUT /api/borrows/{id}/return` - 归还图书
- `GET /api/borrows/user/{userId}` - 获取用户的借阅记录
- `GET /api/borrows/{id}` - 获取借阅记录详情
- `GET /api/borrows` - 获取所有借阅记录

详细API文档请查看 `API_DOCUMENTATION.md`

## 数据库表结构

项目使用 JPA 自动创建表结构，主要包含以下表：

- `user` - 用户表
- `category` - 分类表
- `book` - 图书表
- `borrow` - 借阅记录表

## 配置说明

### application.yml 主要配置

- **数据库连接**: 配置 MySQL 连接信息
- **JPA**: 自动更新表结构（`ddl-auto: update`）
- **日志**: 开发环境显示 SQL 日志
- **CORS**: 已配置允许跨域请求

### 安全配置

- Spring Security 已配置，但当前允许所有请求（便于前后端分离开发）
- 密码使用 BCrypt 加密
- 生产环境建议添加 JWT 认证

## 开发建议

1. **生产环境配置**:
   - 修改 CORS 配置，限制允许的域名
   - 添加 JWT 认证机制
   - 配置 HTTPS
   - 关闭 SQL 日志输出

2. **数据库**:
   - 生产环境建议使用 `ddl-auto: validate` 或 `none`
   - 定期备份数据库

3. **日志**:
   - 生产环境建议使用日志框架（如 Logback）
   - 配置日志级别和输出格式

## 常见问题

### 1. 数据库连接失败

检查：
- MySQL 服务是否启动
- 数据库 `library` 是否已创建
- 用户名和密码是否正确
- 连接URL中的参数是否正确

### 2. 端口被占用

修改 `application.yml` 中的 `server.port` 配置

### 3. CORS 跨域问题

已配置允许所有源，如需限制，修改 `SecurityConfig.java` 中的配置

## 许可证

MIT License

## 作者

图书管理系统开发团队



