# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/account-service/account/pom.xml backend/services/account-service/account/
RUN mvn -f backend/services/account-service/account/pom.xml dependency:go-offline -B

COPY backend/services/account-service/account/src backend/services/account-service/account/src
RUN mvn clean package -f backend/services/account-service/account/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/account-service/account/target/*.jar app.jar
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Xss256k", \
    "-jar", "app.jar"]
