FROM eclipse-temurin:21-jdk AS build

WORKDIR /build

COPY build.gradle.kts settings.gradle.kts /build/

COPY . /build
RUN ./gradlew build -x test --parallel


FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /build/build/libs/*.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
