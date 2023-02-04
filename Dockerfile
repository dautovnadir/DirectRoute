FROM openjdk:8-jdk-alpine
COPY ./ /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/directRoute-0.0.1-SNAPSHOT.jar"]
