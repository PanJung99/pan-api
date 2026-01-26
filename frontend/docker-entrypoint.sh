#!/bin/sh
set -e

# 设置默认后端地址（如果未提供环境变量）
BACKEND_URL=${BACKEND_URL:-http://localhost:8546}

# 使用 envsubst 替换 nginx.conf 中的环境变量
envsubst '${BACKEND_URL}' < /etc/nginx/templates/nginx.conf.template > /etc/nginx/conf.d/default.conf

# 启动 nginx
exec nginx -g 'daemon off;'
