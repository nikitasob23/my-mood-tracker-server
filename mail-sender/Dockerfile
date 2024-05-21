FROM alpine:3.19.1

RUN apk add openjdk17
COPY my-mood-tracker-server/mail-sender/mail-sender-1.0.0.jar app/mail-sender-1.0.0.jar

ENTRYPOINT ["java", "-jar", "app/mail-sender-1.0.0.jar"]