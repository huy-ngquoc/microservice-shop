# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/account-service/account/pom.xml backend/services/account-service/account/
COPY backend/services/auth-service/auth/pom.xml backend/services/auth-service/auth/
COPY backend/services/cart-service/cart/pom.xml backend/services/cart-service/cart/
COPY backend/services/image-service/image/pom.xml backend/services/image-service/image/
COPY backend/services/inventory-service/inventory/pom.xml backend/services/inventory-service/inventory/
COPY backend/services/notification-service/notification/pom.xml backend/services/notification-service/notification/
COPY backend/services/order-service/order/pom.xml backend/services/order-service/order/
COPY backend/services/payment-service/payment/pom.xml backend/services/payment-service/payment/
COPY backend/services/fake-payment/payment/pom.xml backend/services/fake-payment/payment/
COPY backend/services/rating-service/rating/pom.xml backend/services/rating-service/rating/
COPY backend/services/recommendation-service/recommendation/pom.xml backend/services/recommendation-service/recommendation/

RUN mvn -f backend/services/pom.xml dependency:go-offline -B


COPY backend/services/account-service/account/src backend/services/account-service/account/src

COPY backend/services/java-formatter.xml backend/services/java-formatter.xml

RUN mvn clean package -f backend/services/pom.xml -pl account-service -am -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/account-service/account/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
