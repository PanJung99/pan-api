# pan-api

TODO
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