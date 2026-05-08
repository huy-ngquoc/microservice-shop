# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của order-service
COPY backend/services/order-service/order/pom.xml backend/services/order-service/order/
RUN mvn -f backend/services/order-service/order/pom.xml dependency:go-offline -B

COPY backend/services/order-service/order/src backend/services/order-service/order/src
RUN mvn clean package -f backend/services/order-service/order/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/order-service/order/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]