FROM openjdk:11-jdk-slim
WORKDIR /app
COPY /build/libs/server.jar .
ENTRYPOINT ["java", "-jar", "server.jar"]
