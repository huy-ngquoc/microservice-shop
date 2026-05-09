# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/payment-service/payment/pom.xml backend/services/payment-service/payment/
RUN mvn -f backend/services/payment-service/payment/pom.xml dependency:go-offline -B

COPY backend/services/payment-service/payment/src backend/services/payment-service/payment/src
RUN mvn clean package -f backend/services/payment-service/payment/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/payment-service/payment/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
