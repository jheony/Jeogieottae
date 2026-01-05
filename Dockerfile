FROM gradle:8.14.3-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle --no-daemon bootJar -x test

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
