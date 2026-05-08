# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Chỉ copy đúng pom.xml và source code của payment-service
COPY backend/services/fake-payment/payment/pom.xml backend/services/fake-payment/payment/
RUN mvn -f backend/services/fake-payment/payment/pom.xml dependency:go-offline -B

COPY backend/services/fake-payment/payment/src backend/services/fake-payment/payment/src
RUN mvn clean package -f backend/services/fake-payment/payment/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/fake-payment/payment/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]