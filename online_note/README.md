# 在线留言板管理系统 - 运行说明

## 一、项目简介

本项目是一款基于Spring Boot的在线留言板管理系统，包含基础留言管理功能和AI智能助手拓展功能。适合计算机专业课程设计、实训等场景使用。

### 主要功能

#### 1. 用户端功能
- 📝 留言提交（支持匿名留言）
- 📋 留言列表展示（分页）
- 🔍 留言详情查看
- 🤖 AI辅助功能：
  - 留言模板推荐
  - AI内容润色
  - AI违规检测

#### 2. 管理员端功能
- 🔐 管理员登录认证
- 💬 留言综合管理（查看、删除、筛选）
- 📝 回复管理（添加、编辑、删除回复）
- 🤖 AI智能助手：
  - 智能分类留言
  - 自动生成回复
  - 批量违规检测
  - 数据统计分析

---

## 二、环境要求

### 运行环境
- JDK 1.8 或更高版本
- MySQL 5.7 或更高版本（已测试MySQL 8.0）
- Maven 3.6 或更高版本
- Tomcat 9.x（可选，项目已内嵌Spring Boot）

### 开发工具（推荐）
- IntelliJ IDEA（推荐）
- Eclipse
- VS Code

---

## 三、数据库配置

### 1. 创建数据库

打开MySQL命令行或使用Navicat等工具，执行以下SQL：

```sql
CREATE DATABASE IF NOT EXISTS online DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 导入初始化脚本

数据库脚本位于：`src/main/resources/db/script.sql`

可以通过以下方式导入：

**方式一：命令行导入**
```bash
mysql -u root -p online < src/main/resources/db/script.sql
```

**方式二：Navicat导入**
1. 连接到MySQL
2. 选择`online`数据库
3. 右键选择"运行SQL文件"
4. 选择`script.sql`文件

### 3. 修改数据库连接配置

打开`src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root          # 修改为你的MySQL用户名
    password: root         # 修改为你的MySQL密码
```

---

## 四、项目启动

### 方式一：IDE启动（推荐）

1. **导入项目**
   - IntelliJ IDEA：File → Open → 选择项目根目录
   - 等待Maven自动下载依赖

2. **配置JDK**
   - 确保JDK版本为1.8或更高
   - File → Project Structure → Project SDK

3. **启动项目**
   - 找到主类：`MessageBoardApplication.java`
   - 右键点击，选择"Run 'MessageBoardApplication'"

4. **访问项目**
   - 用户端：http://localhost:8080/user/index
   - 管理后台：http://localhost:8080/admin/login

### 方式二：命令行启动

1. **打包项目**
   ```bash
   cd 项目根目录
   mvn clean package
   ```

2. **运行JAR包**
   ```bash
   java -jar target/message-board-1.0.0.jar
   ```

3. **访问项目**
   - 用户端：http://localhost:8080/user/index
   - 管理后台：http://localhost:8080/admin/login

---

## 五、默认测试账号

项目已内置测试数据，包括：

### 管理员账号

| 账号 | 密码 | 说明 |
|------|------|------|
| admin | admin123 | 超级管理员 |
| test | test123 | 测试账号 |

---

## 六、项目结构

```
message-board/
├── pom.xml                 # Maven配置文件
├── src/
│   └── main/
│       ├── java/
│       │   └── com/online/messageboard/
│       │       ├── MessageBoardApplication.java  # 启动类
│       │       ├── entity/                       # 实体类
│       │       │   ├── Message.java              # 留言实体
│       │       │   ├── Reply.java                # 回复实体
│       │       │   └── Admin.java                # 管理员实体
│       │       ├── mapper/                       # MyBatis接口
│       │       │   ├── MessageMapper.java
│       │       │   ├── ReplyMapper.java
│       │       │   └── AdminMapper.java
│       │       ├── service/                      # 业务接口
│       │       │   ├── MessageService.java
│       │       │   ├── ReplyService.java
│       │       │   └── AdminService.java
│       │       ├── service/impl/                 # 业务实现
│       │       │   ├── MessageServiceImpl.java
│       │       │   ├── ReplyServiceImpl.java
│       │       │   └── AdminServiceImpl.java
│       │       ├── controller/                   # 控制器
│       │       │   ├── UserController.java       # 用户端控制器
│       │       │   ├── AdminController.java      # 管理员控制器
│       │       │   └── ManageController.java    # 后台管理控制器
│       │       ├── util/                         # 工具类
│       │       │   ├── AIUtil.java               # AI模拟工具
│       │       │   ├── DateUtil.java             # 日期工具
│       │       │   └── WebUtil.java              # Web工具
│       │       └── exception/                    # 异常处理
│       │           └── GlobalExceptionHandler.java
│       ├── resources/
│       │   ├── application.yml                  # 应用配置
│       │   ├── mapper/                           # MyBatis XML
│       │   │   ├── MessageMapper.xml
│       │   │   ├── ReplyMapper.xml
│       │   │   └── AdminMapper.xml
│       │   ├── static/                           # 静态资源
│       │   │   ├── css/style.css
│       │   │   ├── js/user.js
│       │   │   └── js/manage.js
│       │   ├── templates/                        # HTML模板
│       │   │   ├── user/                        # 用户端页面
│       │   │   │   ├── index.html
│       │   │   │   ├── add.html
│       │   │   │   └── detail.html
│       │   │   ├── admin/                       # 管理员页面
│       │   │   │   └── login.html
│       │   │   └── manage/                      # 后台管理页面
│       │   │       ├── index.html
│       │   │       ├── message.html
│       │   │       ├── reply.html
│       │   │       └── statistics.html
│       │   └── db/                              # 数据库脚本
│       │       └── script.sql
│       └── test/                                # 测试类
└── README.md                                    # 项目说明
```

---

## 七、功能使用指南

### 用户端

1. **提交留言**
   - 访问首页，点击"我要留言"
   - 填写姓名（选填）、联系方式（选填）、留言内容（必填）
   - 可使用AI辅助功能：
     - 点击"咨询模板/建议模板/感谢模板"自动填充
     - 点击"AI润色"优化内容
     - 点击"AI检测"检查违规内容
   - 点击"提交留言"完成

2. **查看留言**
   - 首页展示所有留言列表
   - 点击留言卡片查看详情
   - 详情页显示留言内容和管理员回复

### 管理员端

1. **登录**
   - 访问登录页
   - 输入账号：admin
   - 输入密码：admin123

2. **留言管理**
   - 查看所有留言
   - 按状态筛选（未处理/已回复）
   - 单条或批量删除留言
   - 回复用户留言

3. **回复管理**
   - 查看所有已回复的留言
   - 编辑回复内容
   - 删除回复

4. **AI智能助手**
   - 查看数据统计面板
   - 批量智能分类留言
   - 批量检测违规内容
   - 刷新统计数据

---

## 八、AI功能说明

### 通义千问AI（推荐）

项目已集成阿里云百炼平台的通义千问API，具备真实的AI能力：

1. **内容润色**
   - 智能优化语句，提升表达质量
   - 修正语法错误和标点符号
   - 保持原意的同时使内容更流畅

2. **智能分类**
   - 基于AI理解内容含义
   - 精确分类为：咨询、建议、投诉、其他

3. **违规检测**
   - 智能识别敏感、不当内容
   - 保护平台健康环境

4. **回复生成**
   - 根据留言内容智能生成专业回复
   - 风格礼貌，内容详细，专业可靠

#### 配置通义千问API

1. **获取API Key**
   - 访问阿里云百炼平台：https://bailian.console.aliyun.com/
   - 注册账号并创建应用
   - 获取API Key

2. **修改配置**
   
   打开`src/main/resources/application.yml`，配置AI相关参数：
   
   ```yaml
   # 通义千问AI API配置
   ai:
     tongyi:
       api-key: sk-xxxxxxxxx  # 替换为你的API Key
       model: qwen-turbo  # 可选模型：qwen-turbo、qwen-plus、qwen-max
       enable: true  # 是否启用真实API
   ```

3. **启用/禁用**
   - `enable: true` → 使用真实通义千问API
   - `enable: false` → 使用本地模拟AI

### 本地模拟AI（备用）

当API不可用时，系统自动回退到本地模拟模式，无需外网连接：

1. **内容润色**
   - 自动修正标点符号
   - 优化语句通顺度
   - 规范化格式

2. **智能分类**
   - 咨询类：包含"请问"、"如何"、"怎么"等关键词
   - 建议类：包含"建议"、"希望"、"改进"等关键词
   - 投诉类：包含"投诉"、"不满"、"问题"等关键词
   - 其他：无法分类的内容

3. **违规检测**
   - 检测敏感词汇
   - 标记违规内容

4. **回复生成**
   - 根据留言类型自动生成回复模板
   - 支持编辑后再提交

### 技术实现细节

系统架构如下：
- `TongyiAIConfig.java` → AI配置类
- `TongyiAIUtil.java` → 通义千问API调用
- `AIUtil.java` → 统一AI接口（优先真实API，失败回退）

### 扩展：其他AI接口

如需对接其他AI接口，可参考`TongyiAIUtil.java`的实现方式：
- 文心一言API
- 讯飞星火API
- OpenAI API

---

## 九、常见问题

### 1. 启动报错：端口被占用
```bash
# 查找占用端口的进程
netstat -ano | findstr :8080

# 结束进程（PID为进程ID）
taskkill /PID <PID> /F
```

### 2. 数据库连接失败
- 检查MySQL服务是否启动
- 检查`application.yml`中的数据库配置是否正确
- 确认数据库名称为`online`

### 3. 页面样式异常
- 清除浏览器缓存
- 确认静态资源路径正确

---

## 十、技术栈

- **后端**：Spring Boot 2.7.14 + Spring MVC + MyBatis
- **前端**：HTML5 + CSS3 + 原生JavaScript
- **数据库**：MySQL 8.0
- **构建工具**：Maven 3.6
- **JDK**：1.8+

---

## 十一、联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱：support@example.com
- GitHub：https://github.com/example/message-board

---

## 十二、许可

本项目仅供学习和教育目的使用。

---

**🎉 祝您使用愉快！**
