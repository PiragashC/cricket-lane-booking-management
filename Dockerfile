FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --chown=appuser:appuser target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
