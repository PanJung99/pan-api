# 多阶段构建 Dockerfile for 前端项目

# 第一阶段：构建阶段
FROM node:20-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 package 文件
COPY package.json package-lock.json .npmrc ./

# 安装依赖（使用 npm ci 以获得更快的、可靠的、可重复的构建）
RUN npm ci --legacy-peer-deps

# 复制源代码
COPY . .

# 构建应用
RUN npm run build

# 第二阶段：生产阶段
FROM nginx:alpine

# 安装 envsubst（用于环境变量替换）
RUN apk add --no-cache gettext

# 创建 nginx 模板目录
RUN mkdir -p /etc/nginx/templates

# 复制构建产物到 nginx 目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 nginx 配置模板
COPY nginx.conf /etc/nginx/templates/nginx.conf.template

# 复制启动脚本
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# 暴露端口
EXPOSE 80

# 设置环境变量默认值
ENV BACKEND_URL=http://localhost:8546

# 使用自定义启动脚本
ENTRYPOINT ["/docker-entrypoint.sh"]
