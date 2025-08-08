# Use an official Maven image to build the app
# 1. Use Maven to build the app, with JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Use a lightweight Java runtime to run the JAR
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# EXPOSE is optional (for docs/clarity only, Render detects port from Spring)
EXPOSE 8080

# 3. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
