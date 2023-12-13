FROM openjdk:17-jdk-slim

COPY build/libs/Expenz-0.0.1-SNAPSHOT.jar Expenz.jar

EXPOSE 8080

CMD ["java", "-jar", "Expenz.jar"]
