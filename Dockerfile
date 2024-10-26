FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /app

COPY . /app

RUN mvn clean package -Dmaven.test.skip=true

EXPOSE 8080

CMD ["java", "-jar", "target/tic-tac-toe-server-0.0.1-SNAPSHOT.jar"]
