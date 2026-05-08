# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của inventory-service
COPY backend/services/notification-service/notification/pom.xml backend/services/notification-service/notification/
RUN mvn -f backend/services/notification-service/notification/pom.xml dependency:go-offline -B

COPY backend/services/notification-service/notification/src backend/services/notification-service/notification/src
RUN mvn clean package -f backend/services/notification-service/notification/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/notification-service/notification/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]