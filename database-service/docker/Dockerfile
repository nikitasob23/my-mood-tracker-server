FROM alpine:3.19.1

RUN apk add openjdk17
RUN mkdir app

RUN apk update && apk add curl

COPY database-service/target/database-service-1.0.0.jar app/database-service-1.0.0.jar
COPY scripts/start_container_app.sh app/start_app.sh

RUN chmod +x /app/start_app.sh

ENTRYPOINT ["app/start_app.sh", "app/database-service-1.0.0.jar"]