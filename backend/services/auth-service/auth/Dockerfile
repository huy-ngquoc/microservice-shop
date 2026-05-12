# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/auth-service/auth/pom.xml backend/services/auth-service/auth/
RUN mvn -f backend/services/auth-service/auth/pom.xml dependency:go-offline -B

COPY backend/services/auth-service/auth/src backend/services/auth-service/auth/src
RUN mvn clean package -f backend/services/auth-service/auth/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/auth-service/auth/target/*.jar app.jar
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Xss256k", \
    "-jar", "app.jar"]
