#!/bin/bash

# Build microservice's jars
. scripts/build_microservice_jars.sh

echo "Building Docker images for linux/amd64 and pushing to Docker Hub"
#echo "\n\nStart building database sql docker file"          && docker buildx build --platform linux/amd64 -f database-service/docker/db_sql/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0 --push
echo "\n\nStart building database cache docker file"        && docker buildx build --platform linux/amd64 -f database-service/docker/redis_db_cache/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0 --push
echo "\n\nStart building authorization cache docker file"   && docker buildx build --platform linux/amd64 -f authorization-service/docker/redis_cache/Dockerfile . -t niksob/my-mood-tracker:auth-redis-cache-1.0.0 --push
echo "\n\nStart building config service docker file"        && docker buildx build --platform linux/amd64 -f config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0 --push
echo "\n\nStart building database service docker file"      && docker buildx build --platform linux/amd64 -f database-service/docker/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0 --push
echo "\n\nStart building authorization service docker file" && docker buildx build --platform linux/amd64 -f authorization-service/docker/Dockerfile . -t niksob/my-mood-tracker:authorization-service-1.0.0 --push
echo "\n\nStart building mail sender docker file"           && docker buildx build --platform linux/amd64 -f mail-sender/Dockerfile . -t niksob/my-mood-tracker:mail-sender-1.0.0 --push
echo "\n\nStart building gateway service docker file"       && docker buildx build --platform linux/amd64 -f gateway-service/Dockerfile . -t niksob/my-mood-tracker:gateway-service-1.0.0 --push

# Downloading Docker images to the server
ssh -p 65001 root@80.242.58.161 '
#    echo "PULLING DATABASE SQL DOCKER FILE: "          && docker pull niksob/my-mood-tracker:db-sql-1.0.0
    echo "PULLING DATABASE CACHE DOCKER FILE: "        && docker pull niksob/my-mood-tracker:db-redis-cache-1.0.0
    echo "PULLING AUTHORIZATION CACHE DOCKER FILE: "   && docker pull niksob/my-mood-tracker:auth-redis-cache-1.0.0
    echo "PULLING CONFIG SERVICE DOCKER FILE: "        && docker pull niksob/my-mood-tracker:config-service-1.0.0
    echo "PULLING DATABASE SERVICE DOCKER FILE: "      && docker pull niksob/my-mood-tracker:database-service-1.0.0
    echo "PULLING AUTHORIZATION SERVICE DOCKER FILE: " && docker pull niksob/my-mood-tracker:authorization-service-1.0.0
    echo "PULLING MAIL SENDER DOCKER FILE: "           && docker pull niksob/my-mood-tracker:mail-sender-1.0.0
    echo "PULLING GATEWAY SERVICE DOCKER FILE: "       && docker pull niksob/my-mood-tracker:gateway-service-1.0.0
'

scp -P 65001 /Users/nickworker/Documents/Repo/my-mood-tracker-server/compose-env.yml root@80.242.58.161:/root/my-mood-tracker-server