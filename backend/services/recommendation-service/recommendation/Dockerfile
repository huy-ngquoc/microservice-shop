# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của inventory-service
COPY backend/services/recommendation-service/recommendation/pom.xml backend/services/recommendation-service/recommendation/
RUN mvn -f backend/services/recommendation-service/recommendation/pom.xml dependency:go-offline -B

COPY backend/services/recommendation-service/recommendation/src backend/services/recommendation-service/recommendation/src
RUN mvn clean package -f backend/services/recommendation-service/recommendation/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/recommendation-service/recommendation/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]