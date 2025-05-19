# Base image for the build container
FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder

# Set the working directory to run commands
WORKDIR /project

# Copy the project dependencies
COPY pom.xml /project/

# Download project dependencies
RUN mvn dependency:go-offline

# Copy the project source code
COPY src /project/src

# Build the project and download libraries
RUN mvn -B package -DskipTests

# Base image for the application container (Alpine is used for a smaller image)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory where the JAR will be located
WORKDIR /usr/src/app/

# Copy the built JAR from the builder container
COPY --from=builder /project/target/*.jar /usr/src/app/app.jar

# Expose the port the app will run on
EXPOSE 8443

# Default command when the container runs
CMD [ "java", "-jar", "app.jar" ]