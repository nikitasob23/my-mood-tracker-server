FROM alpine:3.19.1

RUN apk update && apk add --no-cache openjdk17 && apk add --no-cache curl

COPY authorization-service/target/authorization-service-1.0.0.jar app/authorization-service-1.0.0.jar
COPY scripts/docker/start_container_app.sh app/start_app.sh

RUN chmod +x /app/start_app.sh

ENTRYPOINT ["app/start_app.sh", "app/authorization-service-1.0.0.jar"]