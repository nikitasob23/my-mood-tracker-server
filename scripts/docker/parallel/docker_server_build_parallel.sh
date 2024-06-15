#!/bin/bash

# Build microservice's jars
. scripts/jars/parallel/build_microservice_jars_parallel.sh

# Определим общие переменные для репозитория и тега
DOCKER_REPO="niksob/my-mood-tracker"

# Сборка и публикация образов с явным указанием репозитория и тега
docker buildx build --platform linux/amd64 -f database-service/docker/db_sql/Dockerfile . -t ${DOCKER_REPO}:db-sql-1.0.0 --push &
docker buildx build --platform linux/amd64 -f database-service/docker/adminer/Dockerfile . -t ${DOCKER_REPO}:adminer-1.0.0 --push &
docker buildx build --platform linux/amd64 -f database-service/docker/redis_db_cache/Dockerfile . -t ${DOCKER_REPO}:db-redis-cache-1.0.0 --push &
docker buildx build --platform linux/amd64 -f authorization-service/docker/redis_cache/Dockerfile . -t ${DOCKER_REPO}:auth-redis-cache-1.0.0 --push &
docker buildx build --platform linux/amd64 -f config-service/Dockerfile . -t ${DOCKER_REPO}:config-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f database-service/docker/Dockerfile . -t ${DOCKER_REPO}:database-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f authorization-service/docker/Dockerfile . -t ${DOCKER_REPO}:authorization-service-1.0.0 --push &
docker buildx build --platform linux/amd64 -f mail-sender/Dockerfile . -t ${DOCKER_REPO}:mail-sender-1.0.0 --push &
docker buildx build --platform linux/amd64 -f gateway-service/Dockerfile . -t ${DOCKER_REPO}:gateway-service-1.0.0 --push &
wait

ssh -p 65001 root@80.242.58.161 '
    docker pull niksob/my-mood-tracker:db-sql-1.0.0 &
    docker pull niksob/my-mood-tracker:adminer-1.0.0 &
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