# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/product-service/pom.xml backend/services/product-service/
RUN mvn -f backend/services/product-service/pom.xml dependency:go-offline -B

COPY backend/services/product-service/src backend/services/product-service/src
RUN mvn clean package -f backend/services/product-service/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/product-service/target/*.jar app.jar
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Xss256k", \
    "-jar", "app.jar"]
