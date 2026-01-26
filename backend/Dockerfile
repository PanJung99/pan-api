# 构建阶段
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# 利用pom.xml缓存，只有pom变化时才失效
COPY pom.xml .
COPY pan-api-*/pom.xml ./

RUN mvn dependency:go-offline -B || true

COPY . .
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

# curl tzdata(时区数据)
RUN apk add --no-cache curl tzdata

# 设置时区环境变量
ENV TZ=Asia/Shanghai

WORKDIR /app

COPY --from=builder /build/pan-api-web/target/pan-api-web-*.jar app.jar

# 创建非 root 用户
RUN addgroup -S spring && adduser -S spring -G spring

RUN mkdir -p /data/logs && \
    chown -R spring:spring /app /data

USER spring:spring

# 暴露端口
EXPOSE 8546

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8546/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
