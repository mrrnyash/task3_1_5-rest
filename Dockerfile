FROM openjdk:11
COPY target/task3_1_5-rest-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]