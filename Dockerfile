# === PASO 1: Compilar la aplicación ===
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# === PASO 2: Configurar el entorno de ejecución (Imagen corregida) ===
FROM eclipse-temurin:17-jre-jammy
COPY --from=build /target/products-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]