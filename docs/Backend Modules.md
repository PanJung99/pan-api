```
PanApi/
├── pan-api-common      # 公共模块 - 通用工具、异常、DTO
├── pan-api-user        # 用户模块 - 用户、认证、余额、充值等功能
├── pan-api-payment     # 支付渠道接入(暂时移除)
├── pan-api-router      # LLM流量调度模块 - 模型路由和智能调度
├── pan-api-web         # 接口服务 - 主应用入口，REST API
├── pan-api-vendor      # 服务商抽象层 - 第三方LLM集成、订单管理
├── pan-api-model       # 平台模型 & 定价规则
└── pan-api-order       # 使用订单 & 结算

```
---

## 详细模块说明

### 1. pan-api-parent (父POM模块)
**职责**: 项目的依赖管理和公共配置

**核心依赖**:
- Spring Boot 3.4.13 & Spring Cloud 2024.0.1
- Spring Security 安全框架
- Mybatis Plus 数据库ORM
- OpenFeign 微服务调用
- Spring WebFlux 异步响应处理
- Caffeine 本地缓存
- MySQL 数据库驱动

**配置内容**:
- Java 版本: 17
- 统一的 Spring Boot 依赖版本管理
- 数据验证、缓存、数据库相关配置

---

### 2. pan-api-common (公共模块)
**职责**: 提供系统级别的通用工具和数据结构

**主要包含**:
- `conf/` - 租户上下文 (`TenantContext`) 用于多租户隔离
- `dto/` - 通用数据传输对象 (响应格式、请求参数等)
- `enums/` - 业务枚举定义 (异常码、登录类型等)
- `exceptions/` - 统一异常处理体系
  - `BusinessException` 业务异常
  - `BusinessRespCodeEnum` 异常响应码
- `util/` - 工具类库

**特点**: 
- 不包含具体业务逻辑，纯工具层
- 被所有其他模块依赖

---

### 3. pan-api-user (用户模块)
**职责**: 用户相关的核心业务功能

**主要功能模块**:

#### 用户认证管理
- `service/AuthService` - 用户注册、登录
- `model/User` - 用户实体 (支持密码/第三方登录)
- `service/UserService` - 用户查询和信息管理

#### 余额管理 (重要)
- `service/UserBalanceService` - 余额查询、扣费、增加
- `model/Balance` - 用户余额记录
- `dao/BalanceMapper` - 支持原子操作: 余额扣除、增加、转账

#### 充值计划管理
- `service/RechargePlanService` - 管理充值套餐 (名称、价格、说明等)
- `model/RechargePlan` - 充值套餐实体
- `dao/RechargePlanMapper` - 查询活跃套餐

#### API密钥管理
- `service/ApiKeyService` - 为用户生成和管理API密钥
- `model/ApiKey` - API密钥实体
- 支持多密钥、密钥启用/禁用、额度配额等

#### 租户管理
- `model/TenantAdmin` - 租户管理员映射关系
- `dao/TenantAdminMapper` - 用户与租户的多对多关系

**数据库表**:
- `us_user` - 用户表
- `us_balance` - 余额表
- `us_api_key` - API密钥表
- `recharge_plan` - 充值套餐表
- `tenant_admin` - 租户管理员表

---

### 4. pan-api-router (LLM流量调度模块)
**职责**: 智能路由和流量调度核心引擎

**核心功能**:

#### 模型适配器模式
- `service/ModelAdapter` 接口 - 定义模型适配规范
- `service/impl/VendorModelAdapter` - 具体实现
- 支持多个LLM服务商的模型无缝切换

#### 路由和调度
- `service/RouterService` - 主路由引擎
  - 请求分析和模型选择
  - 支持流式和非流式请求
  - 智能负载均衡选择模型实例
  
#### 流量处理
- `util/OpenAIStreamAccumulator` - 流式响应累积处理
- 支持 `stream=true` 的SSE长连接流式响应
- 支持普通的同步非流式响应

#### 价格计算
- 预估费用计算 (`estimatedPrice`)
- 实际费用计算 (`calculatePrice`)
- 图片生成费用计算 (`calculateChatImagePrice`)
- 基于Token数量的精准计价

#### 流程控制
- 余额预验证和扣费
- 支持图片生成功能的额外费用
- 订单创建记录 (通过 `ApiRequestOrderService`)

**关键依赖**:
- `jtokkit` - Token计数库 (精确计算LLM输入输出token)
- `javacv` & `ffmpeg` - 视频/图片处理
- 依赖 `pan-api-vendor` 获取模型和服务商信息

---

### 5. pan-api-vendor (服务商抽象层)
**职责**: 第三方LLM服务集成的抽象层

**主要功能**:

#### 服务商管理
- `model/Vendor` - 服务商实体 (如OpenAI, Claude, Gemini等)
- 服务商配置、密钥、端点管理

#### 模型管理
- `model/Model` - 模型实体 (gpt-4, claude-3等)
  - 关联到具体服务商
  - 存储模型配置 (API参数、版本等)
- `model/ModelTag` - 模型标签分类 (用于路由)
- `service/ModelTypeService` - 模型类型管理 (chatgpt, glm, gemini等)

#### 订单管理 (重要)
- `service/ApiRequestOrderService` - API请求订单创建和查询
- `model/ApiRequestOrder` - 订单记录
  - 追踪每个API请求
  - 记录用户、API密钥、模型、费用、供应商订单号
  - 支持今日统计查询 (token数、请求数)

#### 费用相关
- 模型的价格配置 (输入/输出token价格)
- 与 `pan-api-router` 配合计算实际费用

**数据库表**:
- `vendor` - 服务商表
- `model` - 模型表
- `model_tag` - 模型标签表
- `api_request_order` - API请求订单表

---

### 6. pan-api-web (主应用/接口服务)
**职责**: 系统的启动入口和REST API网关

**主要功能**:

#### 应用启动
- `PanApiApplication` - Spring Boot 主启动类
- 配置 Mapper 扫描、Feign 客户端、调度任务、异步处理

#### 安全认证框架
- `config/auth/GlobalSecurityConfig` - Spring Security 全局配置
  - 两条安全链: `/be/**` (JWT认证) 和 `/api/**` (API密钥认证)
- `config/auth/JwtAuthFilter` - JWT令牌验证过滤器
  - 从Cookie中提取JWT
  - 支持普通用户和租户管理员两种身份
- `config/auth/ApiKeyAuthFilter` - API密钥验证过滤器
  - 用于第三方集成调用

#### REST API端点
- `web/be/*` - 前端后台管理API
  - `UserController` - 用户个人信息、API密钥、余额查询
  - `ChatController` - AI对话接口 (流式/非流式)
  - `RechargeController` - 充值相关接口
- `web/api/*` - 第三方API接口
  - OpenAI兼容的聊天完成 (Chat Completion) API
  - 支持函数调用、角色定义等高级功能

#### 服务层
- `service/BackendService` - 前端查询服务
  - 用户信息聚合
  - 模型列表、充值套餐列表
  - 今日调用统计

#### 工具和配置
- `util/JwtUtil` - JWT的生成和验证工具
- `config/JacksonConfig` - JSON序列化配置
  - BigDecimal 避免科学计数法
  - LocalDateTime 统一格式化
- `config/TenantConfigService` - 租户个性化配置 (支付宝/微信配置等)

#### 异常处理
- 统一的异常处理器
- 自定义认证异常处理
- 权限不足处理

**启用的特性**:
- `@EnableScheduling` - 定时任务
- `@EnableFeignClients` - 微服务调用
- `@Async` - 异步处理

**关键API路由**:
- `/be/auth/login` - 用户登录
- `/be/auth/register` - 用户注册
- `/be/user/profile` - 用户信息
- `/be/chat/completion` - AI对话 (流式)
- `/be/recharge/plans` - 充值套餐列表
- `/api/v1/chat/completions` - OpenAI兼容API (第三方调用)

---

## 模块间依赖关系

```
pan-api-web (主应用)
    ├── 依赖 → pan-api-router (路由调度)
    │            └── 依赖 → pan-api-vendor (服务商)
    │                        └── 依赖 → pan-api-common
    ├── 依赖 → pan-api-user (用户管理)
    │            └── 依赖 → pan-api-common
    ├── 依赖 → pan-api-common (公共工具)
    └── 依赖 → pan-api-parent (依赖管理)
```

---

## 核心业务流程

### 用户充值 + API调用流程

1. **用户充值**: 
   - 前端调用 `/be/recharge/plans` 获取套餐列表 (来自 `pan-api-vendor`)
   - 选择套餐，创建订单，支付成功后增加余额 (在 `pan-api-user`)

2. **获取API密钥**:
   - 用户在个人资料页面生成API密钥 (在 `pan-api-user`)
   
3. **调用AI接口**:
   - **前端调用**: 
     - `/be/chat/completion` (JWT认证)
     - 请求由 `pan-api-router` 处理路由和调度
     - `pan-api-vendor` 提供具体模型信息
     - 费用从用户余额扣除 (通过 `pan-api-user`)
     - 订单记录创建 (在 `pan-api-vendor`)
   
   - **第三方调用** (OpenAI兼容):
     - `/api/v1/chat/completions` (API密钥认证)
     - 相同的路由、模型、计价逻辑

4. **费用计算和扣费**:
   - 请求进入 `pan-api-router` 的 `RouterService`
   - 预估费用，验证余额
   - 调用LLM获取响应
   - 计算实际费用 (输入/输出token)
   - 从余额中扣除 (原子操作)
   - 创建订单记录

---

## 技术栈总结

| 层级 | 技术 | 说明 |
|------|------|------|
| 框架 | Spring Boot 3.4.13, Spring Cloud 2024.0.1 | 微服务基础 |
| 安全 | Spring Security, JWT | 用户认证和授权 |
| 数据库 | MySQL, Mybatis Plus | ORM框架 |
| 远程调用 | OpenFeign | 调用第三方LLM API |
| 缓存 | Caffeine | 本地缓存 |
| 异步处理 | Spring WebFlux, @Async | 非阻塞IO和异步任务 |
| Token计数 | jtokkit | 精确的token统计 |
| 多媒体处理 | JavaCV, FFmpeg | 视频/图片处理 |

---

## 扩展点

1. **新增LLM服务商**: 
   - 在 `pan-api-vendor` 增加新的Vendor实体
   - 实现 `ModelAdapter` 接口的新适配器
   
2. **自定义计价策略**:
   - 修改 `pan-api-router` 的计价逻辑
   
3. **新增功能**:
   - 充值(待完成 pan-api-payment)
   - 文件上传处理
   - 其他AI功能集成
```