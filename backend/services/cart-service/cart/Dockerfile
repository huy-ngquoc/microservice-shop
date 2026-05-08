# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/cart-service/cart/pom.xml backend/services/cart-service/cart/
RUN mvn -f backend/services/cart-service/cart/pom.xml dependency:go-offline -B

COPY backend/services/cart-service/cart/src backend/services/cart-service/cart/src
RUN mvn clean package -f backend/services/cart-service/cart/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/cart-service/cart/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
