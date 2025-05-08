FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /build
COPY . /build

RUN ls /build

# 使用 Maven 构建项目并将 jar 包内容提取出来
RUN mvn clean package -DskipTests && \
    mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

FROM registry.fit2cloud.com/metersphere/alpine-openjdk21-jre

LABEL maintainer="FIT2CLOUD <support@fit2cloud.com>"

ARG MS_VERSION=dev
ARG DEPENDENCY=target/dependency

# 从 build 阶段中拷贝相应文件夹到最终镜像中
COPY --from=build /build/${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build /build/${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build /build/${DEPENDENCY}/META-INF /app/META-INF

RUN mkdir -p /opt/metersphere/logs && chmod -R 777 /opt/metersphere/logs

ENV JAVA_CLASSPATH=/app:/app/lib/*
ENV JAVA_MAIN_CLASS=io.metersphere.mcp.server.Application
ENV AB_OFF=true
ENV MS_VERSION=${MS_VERSION}
ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Djava.awt.headless=true \
    --add-opens java.base/jdk.internal.loader=ALL-UNNAMED \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    --add-opens java.base/java.io=ALL-UNNAMED"

CMD ["/deployments/run-java.sh"]

# docker buildx build -t metersphere-mcp-server:latest .

# docker rm -f metersphere-mcp-server >/dev/null 2>&1; docker run -d -p 8088:8088 --name metersphere-mcp-server metersphere-mcp-server:latest
