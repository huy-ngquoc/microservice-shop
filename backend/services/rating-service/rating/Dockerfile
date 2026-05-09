# Stage 1: Build
FROM microservice-shop-shared-base AS build
WORKDIR /app

COPY backend/services/rating-service/rating/pom.xml backend/services/rating-service/rating/
RUN mvn -f backend/services/rating-service/rating/pom.xml dependency:go-offline -B

COPY backend/services/rating-service/rating/src backend/services/rating-service/rating/src
RUN mvn clean package -f backend/services/rating-service/rating/pom.xml -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/services/rating-service/rating/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
