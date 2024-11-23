FROM openjdk:23-jdk-slim

WORKDIR /app

COPY build/libs/codingchallenge-0.0.1-SNAPSHOT.jar /app/my-api.jar

ENTRYPOINT ["java", "-jar", "my-api.jar"]