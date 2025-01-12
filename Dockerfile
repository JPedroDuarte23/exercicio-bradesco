FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN mvn clean package -e

EXPOSE 8080

CMD ["java", "-jar", "target/gerenciador-de-financas-0.0.1-SNAPSHOT.jar"]