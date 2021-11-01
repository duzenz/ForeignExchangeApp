FROM openjdk:8-jdk-alpine
COPY target/exchange.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]