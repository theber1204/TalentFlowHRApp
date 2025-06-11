FROM ubuntu:latest
LABEL authors="Eber Henri"
# Usa una imagen base de OpenJDK
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo WAR generado por Maven
COPY target/TalentFlowHRApp-1.0-SNAPSHOT.war /app/TalentFlowHRApp.war

# Expone el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/TalentFlowHRApp.war"]

ENTRYPOINT ["top", "-b"]