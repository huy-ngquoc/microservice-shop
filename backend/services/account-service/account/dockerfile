# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của account-service
COPY backend/services/account-service/account/pom.xml backend/services/account-service/account/
RUN mvn -f backend/services/account-service/account/pom.xml dependency:go-offline -B

COPY backend/services/account-service/account/src backend/services/account-service/account/src
RUN mvn clean package -f backend/services/account-service/account/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/account-service/account/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]