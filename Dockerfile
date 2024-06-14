# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS builder

EXPOSE 8080

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Stage 2: Setup the runtime configuration
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Define the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
