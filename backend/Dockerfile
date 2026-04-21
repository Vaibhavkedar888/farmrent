# -------------------------------------------------
#  Build stage – compile the Spring Boot app
# -------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven wrapper and pom files first (caches dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the full source tree and build the JAR (skip tests for speed)
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# -------------------------------------------------
#  Runtime stage – run the thin JAR in a minimal JRE
# -------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# Create a non‑root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy the JAR produced in the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Entry point – start the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
