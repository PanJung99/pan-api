# 快速开始

本指南将帮助你快速上手 Pan API。

## 前置要求

- 一个有效的账户
- API Key（在控制台创建）

## 获取 API Key

1. 登录 [控制台](https://console.pan-api.com)
2. 进入"API Keys"页面
3. 点击"创建 API Key"
4. 输入名称（可选）
5. 复制生成的 API Key

> ⚠️ **重要**：API Key 仅显示一次，请妥善保管！

## 发起第一个请求

### cURL

```bash
curl -X POST https://api.pan-api.com/v1/chat/completions \
  -H "Authorization: Bearer YOUR_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gpt-4",
    "messages": [
      {"role": "user", "content": "Hello, world!"}
    ]
  }'
```

### JavaScript

```javascript
const response = await fetch('https://api.pan-api.com/v1/chat/completions', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer YOUR_API_KEY',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    model: 'gpt-4',
    messages: [
      { role: 'user', content: 'Hello, world!' }
    ]
  })
})

const data = await response.json()
console.log(data)
```

### Python

```python
import requests

url = "https://api.pan-api.com/v1/chat/completions"
headers = {
    "Authorization": "Bearer YOUR_API_KEY",
    "Content-Type": "application/json"
}
data = {
    "model": "gpt-4",
    "messages": [
        {"role": "user", "content": "Hello, world!"}
    ]
}

response = requests.post(url, headers=headers, json=data)
print(response.json())
```

## 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "chatcmpl-123",
    "object": "chat.completion",
    "created": 1677652288,
    "model": "gpt-4",
    "choices": [
      {
        "index": 0,
        "message": {
          "role": "assistant",
          "content": "Hello! How can I help you today?"
        },
        "finish_reason": "stop"
      }
    ],
    "usage": {
      "prompt_tokens": 9,
      "completion_tokens": 12,
      "total_tokens": 21
    }
  }
}
```

## 下一步

- 了解[认证方式](/admin/api-docs/authentication)
- 查看[模型列表](/admin/api-docs/models)
- 阅读[最佳实践](/admin/api-docs/best-practices)
