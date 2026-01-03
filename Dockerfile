# 1단계: Gradle로 빌드
FROM gradle:8.3-jdk17 AS build

WORKDIR /app

# 의존성 캐시 최적화
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle --no-daemon build || true

# 소스 복사
COPY src ./src

# 실제 빌드
RUN gradle --no-daemon bootJar

# 2단계: 런타임
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 빌드된 jar 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
