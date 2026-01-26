# Pan API Frontend

Pan API 前端项目，基于 Vite + React + TypeScript + Tailwind CSS + shadcn/ui 构建。

## 技术栈

- **Vite** - 构建工具
- **React 18** - UI 框架
- **TypeScript** - 类型系统
- **Tailwind CSS** - 样式框架
- **shadcn/ui** - UI 组件库
- **React Router** - 路由管理
- **Zustand** - 状态管理
- **Axios** - HTTP 客户端

## 项目结构

```
src/
├── pages/           # 页面组件
│   ├── landing/     # 公开页面（首页、登录、注册）
│   ├── user/        # 用户后台页面
│   └── admin/       # 管理后台页面
├── components/      # 组件
│   ├── ui/          # shadcn/ui 基础组件
│   ├── layout/      # 布局组件
│   └── common/      # 通用业务组件
├── services/        # API 服务层
├── hooks/           # 自定义 Hooks
├── router/          # 路由配置
├── store/           # 状态管理
├── types/           # TypeScript 类型定义
└── utils/           # 工具函数
```

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 环境配置

开发环境默认代理配置：
- `/be` -> `http://localhost:8080`
- `/admin` -> `http://localhost:8080`
- `/api` -> `http://localhost:8080`

可在 `vite.config.ts` 中修改代理配置。

## 代码规范

- 页面组件中禁止编写大量 CSS
- 页面中禁止定义设计系统样式（颜色、字体、阴影等）
- 优先使用 UI 组件库（shadcn/ui）
- 所有可复用样式必须封装为组件
