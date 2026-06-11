FROM eclipse-temurin:26-jdk
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]