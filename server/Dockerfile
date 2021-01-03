FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY /build/libs/server.jar .
ENTRYPOINT ["java", "-jar", "server.jar"]
