FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY /build/libs/UrlShortener.jar .
ENTRYPOINT ["java", "-jar", "UrlShortener.jar"]
