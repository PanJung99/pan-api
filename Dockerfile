FROM maven:3.8.5-openjdk-17 AS base
COPY ./ /code/
WORKDIR /code
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk
COPY --from=base /code/backend/pan-api-web/target/lattice-core-backend-web-1.0-SNAPSHOT.jar /root
WORKDIR /root
RUN mkdir -p /data/logs
ENV JAVA_OPTS="-Xms500m -Xmx500m"
CMD java $JAVA_OPTS -jar lattice-core-backend-web-1.0-SNAPSHOT.jar
