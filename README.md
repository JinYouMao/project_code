# 在线留言板管理系统

## 项目简介

在线留言板管理系统是一个基于Spring Boot + MyBatis的Web应用，提供用户留言、管理员回复、AI智能助手等功能。系统采用前后端分离架构，前端使用HTML5+CSS3+JavaScript，后端使用Spring Boot框架，数据库使用MySQL。

## 功能特性

### 用户端功能
- 用户注册和登录
- 查看留言列表（分页展示）
- 提交新留言
- 查看留言详情及管理员回复
- AI辅助功能：
  - 内容润色
  - 违规检测
  - 智能分类

### 管理员端功能
- 管理员登录认证
- 首页数据概览
- 留言管理：
  - 查看所有留言
  - 按状态筛选（未处理/已回复）
  - 单条/批量删除留言
  - 标记留言状态
  - AI智能分类
  - 违规检测
- 回复管理：
  - 添加回复
  - 编辑回复
  - 删除回复
  - AI智能生成回复
- 数据统计：
  - 总留言数
  - 未处理留言数
  - 已回复留言数
  - 违规留言数
  - 留言分类统计

## 技术栈

### 后端技术
- **框架**：Spring Boot 2.7.14
- **ORM**：MyBatis 2.3.1
- **数据库**：MySQL 8.0
- **模板引擎**：Thymeleaf
- **工具库**：Lombok, FastJSON, OkHttp
- **JDK版本**：JDK 17

### 前端技术
- HTML5 + CSS3
- 原生JavaScript
- 响应式设计

### 开发工具
- Maven 3.6+
- IntelliJ IDEA（推荐）
- Navicat（数据库管理工具）

## 项目结构

```
online_note/
├── pom.xml                                    # Maven配置文件
├── README.md                                  # 项目说明文档
├── src/
│   └── main/
│       ├── java/
│       │   └── com/online/messageboard/
│       │       ├── MessageBoardApplication.java  # 启动类
│       │       ├── config/                       # 配置类
│       │       │   ├── MyBatisConfig.java        # MyBatis配置
│       │       │   ├── TongyiAIConfig.java       # AI配置
│       │       │   └── WebConfig.java            # Web配置
│       │       ├── controller/                   # 控制器
│       │       │   ├── AdminController.java      # 管理员控制器
│       │       │   ├── ManageController.java     # 后台管理控制器
│       │       │   └── UserController.java       # 用户端控制器
│       │       ├── entity/                       # 实体类
│       │       │   ├── Admin.java                # 管理员实体
│       │       │   ├── Message.java              # 留言实体
│       │       │   ├── Reply.java                # 回复实体
│       │       │   └── User.java                 # 用户实体
│       │       ├── mapper/                       # MyBatis接口
│       │       │   ├── AdminMapper.java
│       │       │   ├── MessageMapper.java
│       │       │   ├── ReplyMapper.java
│       │       │   └── UserMapper.java
│       │       ├── service/                      # 业务接口
│       │       │   ├── AdminService.java
│       │       │   ├── MessageService.java
│       │       │   ├── ReplyService.java
│       │       │   └── UserService.java
│       │       ├── service/impl/                 # 业务实现
│       │       │   ├── AdminServiceImpl.java
│       │       │   ├── MessageServiceImpl.java
│       │       │   ├── ReplyServiceImpl.java
│       │       │   └── UserServiceImpl.java
│       │       ├── util/                         # 工具类
│       │       │   ├── AIUtil.java               # AI工具
│       │       │   ├── DateUtil.java             # 日期工具
│       │       │   ├── TongyiAIUtil.java         # 通义千问工具
│       │       │   └── WebUtil.java              # Web工具
│       │       ├── exception/                    # 异常处理
│       │       │   └── GlobalExceptionHandler.java
│       │       └── interceptor/                  # 拦截器
│       │           └── AdminInterceptor.java     # 管理员登录拦截器
│       └── resources/
│           ├── application.yml                   # 应用配置文件
│           ├── mapper/                           # MyBatis XML映射
│           │   ├── AdminMapper.xml
│           │   ├── MessageMapper.xml
│           │   ├── ReplyMapper.xml
│           │   └── UserMapper.xml
│           ├── static/                           # 静态资源
│           │   ├── css/
│           │   │   └── style.css                 # 样式文件
│           │   └── js/
│           │       ├── manage.js                 # 管理后台JS
│           │       └── user.js                   # 用户端JS
│           └── templates/                        # Thymeleaf模板
│               ├── admin/
│               │   └── login.html                # 管理员登录页
│               ├── manage/
│               │   ├── index.html                # 管理后台首页
│               │   ├── message.html              # 留言管理页
│               │   ├── reply.html                # 回复管理页
│               │   └── statistics.html           # 数据统计页
│               └── user/
│                   ├── index.html                # 用户首页
│                   ├── add.html                  # 提交留言页
│                   ├── detail.html               # 留言详情页
│                   ├── login.html                # 用户登录页
│                   └── register.html             # 用户注册页
└── target/                                      # Maven编译输出
```

## 环境要求

- JDK 17
- Maven 3.6+
- MySQL 5.7+（推荐MySQL 8.0）
- 现代浏览器（Chrome、Firefox、Edge等）

## 快速开始

### 1. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS message_board_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入数据库表
请执行数据库脚本创建表结构并插入初始数据。

#### 修改应用配置
打开 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/message_board_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root      # 修改为你的MySQL用户名
    password: root     # 修改为你的MySQL密码
```

### 2. 配置通义千问AI（可选）

如需要使用真实AI功能，请配置通义千问API：

```yaml
ai:
  tongyi:
    api-key: sk-你的API密钥  # 替换为真实API密钥
    enable: true              # 是否启用真实API
```

### 3. 运行项目

#### 使用IDE运行
1. 使用IntelliJ IDEA打开项目
2. 等待Maven自动下载依赖
3. 找到 `MessageBoardApplication.java`，右键运行
4. 访问项目：
   - 用户端首页：http://localhost:8080/user/index
   - 管理员登录页：http://localhost:8080/admin/login

#### 使用Maven命令行运行
```bash
# 进入项目目录
cd g:\online_note

# 编译运行
mvn spring-boot:run
```

## 测试账号

### 管理员账号
| 账号 | 密码 |
|------|------|
| admin | admin |

### 用户账号
请先通过用户注册页注册新用户。

## 功能使用说明

### 用户端操作

1. **用户注册**
   - 访问用户注册页：http://localhost:8080/user/register
   - 填写用户名、密码、手机号
   - 点击注册按钮

2. **用户登录**
   - 访问用户登录页：http://localhost:8080/user/login
   - 输入用户名和密码
   - 点击登录

3. **查看留言列表**
   - 登录后跳转到首页
   - 可查看所有留言，支持分页

4. **提交新留言**
   - 点击"提交留言"按钮
   - 填写留言内容、联系方式
   - 使用AI辅助功能润色或检测
   - 提交留言

5. **查看留言详情**
   - 在留言列表点击某条留言
   - 查看留言内容和管理员回复

### 管理员端操作

1. **管理员登录**
   - 访问管理员登录页：http://localhost:8080/admin/login
   - 输入账号密码登录

2. **首页概览**
   - 查看系统数据统计
   - 总留言数、未处理数、已回复数、违规数

3. **留言管理**
   - 查看所有留言
   - 按状态筛选
   - AI分类、违规检测
   - 删除留言、回复留言
   - 标记留言状态

4. **回复管理**
   - 添加、编辑、删除回复
   - AI智能生成回复

5. **数据统计**
   - 查看详细的数据统计
   - 留言分类统计

## 常见问题

### 1. 端口被占用
如果8080端口被占用，可以修改 `application.yml` 中的端口：
```yaml
server:
  port: 8081  # 修改为其他端口
```

### 2. 数据库连接失败
- 确认MySQL服务已启动
- 检查 `application.yml` 中的数据库配置
- 确认数据库名称和表已创建

### 3. 页面样式异常
- 请使用现代浏览器访问
- 清除浏览器缓存后刷新页面

## 开发说明

### 数据库实体
- **Admin**：管理员实体
- **User**：用户实体
- **Message**：留言实体
- **Reply**：回复实体

### 核心控制器
- **UserController**：处理用户端请求
- **AdminController**：处理管理员登录
- **ManageController**：处理后台管理功能

### AI工具类
- **TongyiAIUtil**：通义千问API调用工具
- **AIUtil**：AI功能统一入口

## 许可证

本项目仅供学习交流使用。
