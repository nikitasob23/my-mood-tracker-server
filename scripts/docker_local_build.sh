#!/bin/bash

# Build microservice's jars
. scripts/build_microservice_jars.sh

# Build docker microservice images
echo "\n\n\nStart building docker database sql"     && docker build -f database-service/docker/db_sql/Dockerfile . -t niksob/my-mood-tracker:db-sql-1.0.0                     && echo "Docker database sql was build"
echo "\n\n\nStart building docker db redis cache"   && docker build -f database-service/docker/redis_db_cache/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0     && echo "Docker db redis cache was build"
echo "\n\n\nStart building docker auth redis cache" && docker build -f authorization-service/docker/redis_cache/Dockerfile . -t niksob/my-mood-tracker:auth-redis-cache-1.0.0 && echo "Docker auth redis cache was build"

echo "\n\n\nStart building docker config service"        && docker build -f config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0               && echo "Docker config service was build"
echo "\n\n\nStart building docker database service"      && docker build -f database-service/docker/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0           && echo "Docker database service was build"
echo "\n\n\nStart building docker authorization service" && docker build -f authorization-service/Dockerfile . -t niksob/my-mood-tracker:authorization-service-1.0.0 && echo "Docker authorization service was build"
echo "\n\n\nStart building docker gateway service"       && docker build -f gateway-service/Dockerfile . -t niksob/my-mood-tracker:gateway-service-1.0.0             && echo "Docker gateway service was build"
echo "\n\n\nStart building docker mail sender"           && docker build -f mail-sender/Dockerfile . -t niksob/my-mood-tracker:mail-sender-1.0.0                     && echo "Docker mail sender was build"