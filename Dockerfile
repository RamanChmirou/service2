# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/service2-0.0.1-SNAPSHOT.jar app/service2-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app/service2-0.0.1-SNAPSHOT.jar"]