# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/recommendation-service/recommendation/pom.xml backend/services/recommendation-service/recommendation/
RUN mvn -f backend/services/recommendation-service/recommendation/pom.xml dependency:go-offline -B

COPY backend/services/recommendation-service/recommendation/src backend/services/recommendation-service/recommendation/src
RUN mvn clean package -f backend/services/recommendation-service/recommendation/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/recommendation-service/recommendation/target/*.jar app.jar
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Xss256k", \
    "-jar", "app.jar"]
