FROM amazoncorretto:19

ADD target/Gateway-0.0.1-SNAPSHOT.jar gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gateway.jar"]