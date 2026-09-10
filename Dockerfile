# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/service2-0.0.1-SNAPSHOT.jar app/service2-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app/service2-0.0.1-SNAPSHOT.jar"]