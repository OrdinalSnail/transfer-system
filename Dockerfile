FROM openjdk:17
ADD target/transfer-system.jar transfer-system.jar
ENTRYPOINT ["java", "-jar","transfer-system.jar"]
EXPOSE 8080