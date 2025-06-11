# Usa uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado para dentro do container
COPY target/estacApp-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta usada pela aplicação (ajuste se necessário)
EXPOSE 9090

# Comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]