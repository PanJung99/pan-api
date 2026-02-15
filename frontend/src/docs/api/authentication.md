# 认证方式

Pan API 使用 API Key 进行身份验证。

## API Key

API Key 是访问 Pan API 的唯一凭证，所有请求都需要在 HTTP Header 中携带。

### 请求头格式

```
Authorization: Bearer YOUR_API_KEY
```

### 示例

```bash
curl -X POST https://api.pan-api.com/v1/chat/completions \
  -H "Authorization: Bearer sk-xxxxxxxxxxxx" \
  -H "Content-Type: application/json" \
  -d '{"model": "gpt-4", "messages": [{"role": "user", "content": "Hello"}]}'
```

## 获取 API Key

1. 登录控制台
2. 进入"API Keys"页面
3. 点击"创建 API Key"
4. 输入名称（可选）
5. 复制生成的 API Key

## API Key 权限

API Key 拥有以下权限：

- ✅ 调用所有模型接口
- ✅ 查询账户余额
- ✅ 查询用量统计
- ❌ 管理员操作（需要管理员权限）

## 安全建议

### 保护 API Key

- 🔒 不要将 API Key 提交到版本控制系统
- 🔒 不要在前端代码中硬编码 API Key
- 🔒 使用环境变量存储 API Key
- 🔒 定期轮换 API Key

### 环境变量示例

```bash
# .env
PAN_API_KEY=sk-xxxxxxxxxxxx
```

```javascript
const apiKey = process.env.PAN_API_KEY
```

### 限制访问

- 为不同的应用创建不同的 API Key
- 定期审查 API Key 使用情况
- 发现异常立即删除并重新创建

## 错误处理

### 401 Unauthorized

```json
{
  "code": 401,
  "message": "Invalid API key",
  "data": null
}
```

**原因**：
- API Key 无效
- API Key 已过期
- API Key 已被删除

**解决方法**：
- 检查 API Key 是否正确
- 在控制台重新创建 API Key

### 403 Forbidden

```json
{
  "code": 403,
  "message": "Permission denied",
  "data": null
}
```

**原因**：
- API Key 权限不足
- 账户余额不足

**解决方法**：
- 检查账户余额
- 联系管理员提升权限
