FROM openjdk:22-jdk-slim

WORKDIR /app

COPY target/epam-gym-demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
