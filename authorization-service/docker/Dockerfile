FROM alpine:3.19.1

RUN apk add openjdk17
COPY my-mood-tracker-server/authorization-service/authorization-service-1.0.0.jar app/authorization-service-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/authorization-service-1.0.0.jar"]