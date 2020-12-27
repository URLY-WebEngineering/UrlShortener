FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY /build/libs/UrlShortener.jar .
ENTRYPOINT ["java", "-jar", "UrlShortener.jar"]
