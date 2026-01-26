# pan-api

TODO
- docker部署方式 docker-compose
- api_order mapper 字段核对 清除无用字段
- ModelType改为枚举
- 完善datetime格式以及解析逻辑
- api限额、日期
- 雪花算法重新设计
- CI 中自动生成 & 校验 docs/openapi.json
- yaml脱敏
- 清除所有Mysql NOW()
- venderType删除掉或者根据 火山 openai等重构
- 前端创建服务商逻辑不够优雅，希望能在VendorCreateDialog中不写像这种
-  // DeepSeek 表单状态（只实现这一种，其他类型先打样）
   const [deepSeekForm, setDeepSeekForm] = useState<DeepSeekVendorFormValue>({
   name: "",
   apiBaseUrl: "",
   })
- 的代码，而是分别写到不同的像DeepSeekVendorForm的页面中
- 重试策略
超时
限流
fallback



环境变量
LOG_PATH

# Docker Compose 使用指南

## 快速开始

### 1. 配置环境变量

复制环境变量示例文件并修改：

```bash
cp .env.example .env
```

编辑 `.env` 文件，设置以下关键配置：

- `MYSQL_ROOT_PASSWORD`: MySQL root 密码
- `MYSQL_PASSWORD`: 数据库用户密码
- `JWT_SECRET`: JWT 密钥（建议使用强随机字符串）
- `SPRING_DATASOURCE_URL`: 数据库连接 URL（Docker Compose 中使用 `mysql` 作为主机名）
- `SPRING_DATASOURCE_USERNAME`: 数据库用户名
- `SPRING_DATASOURCE_PASSWORD`: 数据库密码

### 2. 启动所有服务

```bash
docker-compose up -d
```

### 3. 查看服务状态

```bash
docker-compose ps
```

### 4. 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### 5. 停止服务

```bash
docker-compose down
```

### 6. 停止并删除数据卷（⚠️ 会删除数据库数据）

```bash
docker-compose down -v
```

## 服务说明

### MySQL 数据库
- **容器名**: `pan-api-mysql`
- **端口**: 3306（可在 `.env` 中配置）
- **数据卷**: `mysql_data`（持久化存储）
- **健康检查**: 自动检查数据库连接

### 后端服务
- **容器名**: `pan-api-backend`
- **端口**: 8546（可在 `.env` 中配置）
- **日志目录**: `/data/logs`（挂载到 `backend_logs` 卷）
- **健康检查**: 检查 `/actuator/health` 端点
- **依赖**: 等待 MySQL 健康检查通过后启动

### 前端服务
- **容器名**: `pan-api-frontend`
- **端口**: 8080（可在 `.env` 中配置）
- **代理配置**: 自动代理后端 API（`/be`, `/be-admin`, `/api`）
- **依赖**: 等待后端服务启动后启动

## 环境变量说明

### 数据库配置

```env
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=pan-api
MYSQL_USER=panapi
MYSQL_PASSWORD=your_db_password
MYSQL_PORT=3306
```

### Spring Boot 配置

```env
# 数据库连接（使用 Docker Compose 服务名）
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/pan-api?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
SPRING_DATASOURCE_USERNAME=panapi
SPRING_DATASOURCE_PASSWORD=your_db_password

# JWT 密钥
JWT_SECRET=your_jwt_secret_key

# JVM 参数（可选）
JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
```

### 端口配置

```env
BACKEND_PORT=8546
FRONTEND_PORT=8080
```

## 访问服务

- **前端**: http://localhost:8080
- **后端 API**: http://localhost:8546
- **Swagger UI**: http://localhost:8546/swagger-ui/index.html
- **Actuator Health**: http://localhost:8546/actuator/health

## 常见问题

### 1. 数据库连接失败

确保：
- MySQL 容器已启动并健康
- `.env` 中的数据库配置正确
- `SPRING_DATASOURCE_URL` 使用 `mysql` 作为主机名（Docker Compose 服务名）

### 2. 前端无法访问后端

检查：
- 后端服务是否正常运行
- `BACKEND_URL` 环境变量是否正确（前端容器内应使用 `http://backend:8546`）

### 3. 健康检查失败

- 检查后端日志：`docker-compose logs backend`
- 确认 Actuator 端点已启用
- 检查 Security 配置是否允许访问 `/actuator/health`

### 4. 端口冲突

如果端口被占用，在 `.env` 文件中修改：
```env
MYSQL_PORT=3307
BACKEND_PORT=8547
FRONTEND_PORT=8081
```

## 数据备份

### 备份数据库

```bash
docker-compose exec mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} pan-api > backup.sql
```

### 恢复数据库

```bash
docker-compose exec -T mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} pan-api < backup.sql
```

## 开发模式

如果需要使用开发环境配置，修改 `docker-compose.yml` 中的环境变量：

```yaml
environment:
  SPRING_PROFILES_ACTIVE: dev  # 改为 dev
```

## 生产部署建议

1. **安全性**:
   - 使用强密码和 JWT 密钥
   - 不要将 `.env` 文件提交到版本控制
   - 考虑使用 Docker Secrets 或外部密钥管理服务

2. **性能**:
   - 根据实际负载调整 JVM 参数
   - 配置数据库连接池大小
   - 启用 Nginx 缓存（前端）

3. **监控**:
   - 配置日志收集
   - 使用监控工具（如 Prometheus + Grafana）
   - 设置告警规则

4. **高可用**:
   - 使用 Docker Swarm 或 Kubernetes
   - 配置数据库主从复制
   - 使用负载均衡器




```
你是一个资深前端工程师，正在开发一个长期维护的商用 Web 项目。

请严格遵守以下规则：

【CSS 与样式规则】
1. 页面组件（pages/*）中禁止编写大量 CSS
2. 页面中禁止定义颜色、字体、阴影、按钮、卡片等设计系统样式
3. 页面中只允许极少量布局相关样式（grid / flex / margin）
4. 不允许在页面中写完整的 UI 视觉方案
5. 优先使用 UI 组件库（Ant Design / shadcn/ui / MUI）
6. 不允许为每个页面单独复制一套 CSS
7. 所有可复用样式必须封装为组件或使用组件库

【组件结构规则】
- 页面 = 组件组合
- 样式应存在于：
  - UI 组件库
  - 通用组件（components/*）
- 页面只负责：
  - 数据请求
  - 状态管理
  - 组件编排

【禁止事项】
- 禁止“设计稿式页面”
- 禁止在一个文件中写数百行 CSS
- 禁止 scoped 样式滥用
- 禁止重复定义 Button / Card / Title 等样式

如果无法在以上约束下完成，请说明原因，而不是违反规则。
```