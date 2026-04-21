# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy the entire workspace to get the backend folder
COPY . .

# Build the backend (skip tests)
RUN mvn clean package -DskipTests -pl backend -am

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/backend/target/*.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
