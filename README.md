# Car Rental Management System

基于 `Spring Boot + MyBatis + MySQL + Redis + JWT` 的汽车租赁管理系统后端示例工程，适合作为简历项目或课程设计的后端基础模板。

## 功能模块

- JWT 权限认证：登录签发 `Access Token` 和 `Refresh Token`，刷新令牌持久化到 Redis，拦截器校验并基于 `ThreadLocal` 透传用户上下文
- 缓存优化：首页广告和热点车辆使用 Cache-Aside 模式写入 Redis，带基础 TTL、随机过期和空值缓存
- 业务管理：车辆信息、客户信息、租赁订单、首页广告位管理
- 数据访问：MyBatis 动态 SQL + PageHelper 分页查询
- 部署示例：提供 Nginx 前后端分离反向代理配置

## 运行环境

- JDK 17
- Maven 3.9+
- MySQL 8.x
- Redis 6.x+

## 启动步骤

1. 创建数据库并执行 [schema.sql](/d:/code/java/car-rental/sql/schema.sql)
2. 按需修改 [application.yml](/d:/code/java/car-rental/src/main/resources/application.yml) 中的 MySQL、Redis、JWT 配置
3. 执行 `mvn spring-boot:run` 或 `mvn clean package`

默认管理员账号：

- 用户名：`admin`
- 密码：`password`

## 主要接口

- `POST /api/auth/login` 登录
- `POST /api/auth/refresh` 刷新 Token
- `GET /api/vehicles` 车辆分页查询
- `GET /api/vehicles/hot` 热门车辆
- `POST /api/orders` 创建订单
- `PUT /api/orders/{id}/status?status=FINISHED` 更新订单状态
- `GET /api/banners/active` 首页广告

## 目录说明

- [src/main/java/com/example/carrental](/d:/code/java/car-rental/src/main/java/com/example/carrental) 核心业务代码
- [src/main/resources/mapper](/d:/code/java/car-rental/src/main/resources/mapper) MyBatis XML
- [web](/d:/code/java/car-rental/web) Vue2 + ElementUI 前端工程
- [sql/schema.sql](/d:/code/java/car-rental/sql/schema.sql) 初始化脚本和索引示例
- [nginx/car-rental.conf](/d:/code/java/car-rental/nginx/car-rental.conf) Nginx 部署示例

## 前端运行

1. 进入 [web](/d:/code/java/car-rental/web)
2. 执行 `npm install`
3. 执行 `npm run serve`
4. 打开 `http://localhost:8081`

开发环境已在 [vue.config.js](/d:/code/java/car-rental/web/vue.config.js) 中将 `/api` 代理到 `http://localhost:8080`
