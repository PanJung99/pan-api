# pan-api

高性能(TODO)多模型统一调度网关
基于 Java 构建的下一代大模型聚合平台。提供统一的标准接口，实现全球主流大模型（LLM）的快速集成、流量调度与资源监控，助力企业构建稳定的 AI 中枢。 

**开发中，请不要直接在生产环境使用**

TODO
- 接入chatgpt 火山引擎
- 将CommonReq重构，符合OpenAI标准格式并抽出来， 将deepseek单独创建一个Req， 编写Common和deepseek的转换器
- 缓存从本地改为redis
- api限额、日期
- 雪花算法重新设计
- jwt完善
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

# 部署

## docker-compose部署

### 1. 配置环境变量

复制环境变量示例文件(.env.example)并修改：

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
# 启动服务
docker-compose up -d

# 查看部署状态
docker-compose ps

# 停止服务
docker-compose down
```

### 3. 访问服务

- **前端**: http://localhost:8080
- **后端 API**: http://localhost:8546
- **Swagger UI**: http://localhost:8546/swagger-ui/index.html
- **Actuator Health**: http://localhost:8546/actuator/health


