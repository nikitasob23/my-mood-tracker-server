FROM alpine:3.19.1

RUN apk add openjdk17
COPY my-mood-tracker-server/gateway-service/gateway-service-1.0.0.jar app/gateway-service-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/gateway-service-1.0.0.jar"]