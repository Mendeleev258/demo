# Первый этап: сборка
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Копируем gradle wrapper и build.gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Копируем исходный код
COPY src src

# Даем права на выполнение gradlew и собираем проект
RUN chmod +x gradlew
RUN ./gradlew build -x test

# Второй этап: запуск
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем JAR из первого этапа
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]