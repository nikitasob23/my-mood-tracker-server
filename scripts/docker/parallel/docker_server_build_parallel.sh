#!/bin/bash

# Build microservice's jars
. scripts/jars/parallel/build_microservice_jars_parallel.sh

echo "Building docker images for linux/amd64 and pushing to Docker Hub"
docker buildx build --platform linux/amd64 -f database-service/docker/db_sql/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0 --push &
docker buildx build --platform linux/amd64 -f database-service/docker/redis_db_cache/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0 --push &
docker buildx build --platform linux/amd64 -f authorization-service/docker/redis_cache/Dockerfile . -t niksob/my-mood-tracker:auth-redis-cache-1.0.0 --push &
docker buildx build --platform linux/amd64 -f config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f database-service/docker/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f authorization-service/docker/Dockerfile . -t niksob/my-mood-tracker:authorization-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f mail-sender/Dockerfile . -t niksob/my-mood-tracker:mail-sender-1.0.0 --push &
docker buildx build --platform linux/amd64 -f gateway-service/Dockerfile . -t niksob/my-mood-tracker:gateway-service-1.0.0 --push &
wait

echo "Downloading docker images to the server"
ssh -p 65001 root@80.242.58.161 '
    docker pull niksob/my-mood-tracker:db-sql-1.0.0 &
    docker pull niksob/my-mood-tracker:db-redis-cache-1.0.0 &
    docker pull niksob/my-mood-tracker:auth-redis-cache-1.0.0 &
    docker pull niksob/my-mood-tracker:config-service-1.0.0 &
    docker pull niksob/my-mood-tracker:database-service-1.0.0 &
    docker pull niksob/my-mood-tracker:authorization-service-1.0.0 &
    docker pull niksob/my-mood-tracker:mail-sender-1.0.0 &
    docker pull niksob/my-mood-tracker:gateway-service-1.0.0 &
    wait
'

scp -P 65001 compose-env.yml root@80.242.58.161:/root/my-mood-tracker-server