FROM alpine:3.19.1

RUN apk add openjdk17
RUN apk update && apk add --no-cache curl
RUN apk add vim

RUN mkdir app
COPY my-mood-tracker-server/config-service/config-service-0.0.1.jar app/config-service-0.0.1.jar

ENTRYPOINT ["java", "-jar", "app/config-service-0.0.1.jar"]