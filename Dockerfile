# Stage 1 — Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Télécharge les dépendances en cache séparé du code source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2 — Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Flags JVM adaptés aux conteneurs 256 MB (Fly.io free tier)
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-jar", "app.jar"]
