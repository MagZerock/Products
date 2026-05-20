# === PASO 1: Compilar la aplicación ===
# Usamos una imagen oficial de Maven con OpenJDK 17 para compilar el código
FROM maven:3.8.5-openjdk-17 AS build

# Copiamos todo el contenido del proyecto al contenedor
COPY . .

# Ejecutamos el comando de Maven para empaquetar el proyecto saltándonos los tests
RUN mvn clean package -DskipTests

# === PASO 2: Configurar el entorno de ejecución ===
# Usamos una imagen mucho más ligera de Java 17 solo para correr el archivo generado
FROM openjdk:17-jdk-slim

# Copiamos el archivo .jar compilado desde el paso anterior hacia este nuevo entorno
COPY --from=build /target/products-0.0.1-SNAPSHOT.jar app.jar

# Informamos el puerto en el que típicamente escucha la app (Render mapeará esto)
EXPOSE 8080

# Comando para ejecutar la aplicación pasando el puerto dinámico de Render
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]