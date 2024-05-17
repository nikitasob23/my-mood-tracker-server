FROM alpine:3.19.1

RUN apk add openjdk17
COPY my-mood-tracker-server/authorization-service/authorization-service-0.0.1.jar app/authorization-service-0.0.1.jar

ENTRYPOINT ["java", "-jar", "app/authorization-service-0.0.1.jar"]