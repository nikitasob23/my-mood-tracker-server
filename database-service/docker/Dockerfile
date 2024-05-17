FROM alpine:3.19.1

RUN apk add openjdk17
RUN mkdir app
COPY my-mood-tracker-server/database-service/database-service-0.0.1.jar app/database-service-0.0.1.jar

ENTRYPOINT ["java", "-jar", "app/database-service-0.0.1.jar"]