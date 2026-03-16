# Car Rental Management System

基于 `Spring Boot + MyBatis + MySQL + Redis + JWT` 的汽车租赁管理系统示例项目，包含后端管理接口和 `Vue 2 + ElementUI` 前端后台。

## 技术栈

- 后端：Spring Boot、MyBatis、MySQL、Redis、JWT、PageHelper
- 前端：Vue 2、Vue Router、Axios、ElementUI
- 部署：Nginx

## 运行环境

- JDK 17
- Maven 3.9+
- Node.js 18+
- MySQL 8.x
- Redis 6.x+

## 后端启动

1. 创建数据库 `car_rental2`，执行 [schema.sql](/d:/code/java/car-rental/sql/schema.sql)
2. 检查 [application.yml](/d:/code/java/car-rental/src/main/resources/application.yml) 中的数据库和 Redis 配置
3. 在项目根目录执行：

```bash
mvn spring-boot:run
```

后端默认地址：`http://localhost:8080`

默认管理员账号：

- 用户名：`admin`
- 密码：`password`

## 前端启动

1. 进入 [web](/d:/code/java/car-rental/web)
2. 安装依赖：

```bash
npm install
```

3. 启动开发环境：

```bash
npm run serve
```

前端默认地址：`http://localhost:8081`

## 主要接口

- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/vehicles`
- `GET /api/vehicles/hot`
- `GET /api/customers`
- `GET /api/orders`
- `GET /api/banners/active`

## 目录说明

- [src/main/java/com/example/carrental](/d:/code/java/car-rental/src/main/java/com/example/carrental) 后端业务代码
- [src/main/resources/mapper](/d:/code/java/car-rental/src/main/resources/mapper) MyBatis XML
- [web](/d:/code/java/car-rental/web) 前端工程
- [sql/schema.sql](/d:/code/java/car-rental/sql/schema.sql) 数据库初始化脚本
- [nginx/car-rental.conf](/d:/code/java/car-rental/nginx/car-rental.conf) Nginx 配置示例
