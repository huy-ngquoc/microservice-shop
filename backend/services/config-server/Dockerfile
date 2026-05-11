# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/config-server/pom.xml backend/services/config-server/
RUN mvn -f backend/services/config-server/pom.xml dependency:go-offline -B

COPY backend/services/config-server/src backend/services/config-server/src
RUN mvn clean package -f backend/services/config-server/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/config-server/target/*.jar app.jar
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Xss256k", \
    "-jar", "app.jar"]
