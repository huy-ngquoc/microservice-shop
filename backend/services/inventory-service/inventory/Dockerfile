# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của inventory-service
COPY backend/services/inventory-service/inventory/pom.xml backend/services/inventory-service/inventory/
RUN mvn -f backend/services/inventory-service/inventory/pom.xml dependency:go-offline -B

COPY backend/services/inventory-service/inventory/src backend/services/inventory-service/inventory/src
RUN mvn clean package -f backend/services/inventory-service/inventory/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/inventory-service/inventory/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]