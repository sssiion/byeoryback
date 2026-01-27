FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
