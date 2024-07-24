# Use an alternative base image with Java 17
FROM openjdk:22-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/epam-gym-demo-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
