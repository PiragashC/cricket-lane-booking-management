# Use an official OpenJDK runtime as the base image
# FROM openjdk:17-jdk-slim

# # Set the working directory inside the container
# WORKDIR /app

# # Copy the application JAR file into the container
# ADD target/cricket_lane_booking_management-1.0.0-SNAPSHOT.jar /app/cricket_lane_booking_management-1.0.0-SNAPSHOT.jar

# # Expose the application's port (adjust as needed)
# EXPOSE 8080

# # Run the application
# CMD ["java", "-jar", "/app/cricket_lane_booking_management-1.0.0-SNAPSHOT.jar"]

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY /target/cricket_lane_booking_management-1.0.0-SNAPSHOT.jar /app/cricket_lane_booking_management-1.0.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","cricket_lane_booking_management-1.0.0-SNAPSHOT.jar"]
