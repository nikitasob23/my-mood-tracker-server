#!/bin/bash

# Build microservice's jars
#. scripts/build_microservice_jars.sh

. scripts/copy_jar_and_dockerfile_on_server.sh

# Build docker microservice images
ssh -p 65001 root@80.242.58.161 '
    docker build -f my-mood-tracker-server/config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0
    docker build -f my-mood-tracker-server/database-service/docker/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0
    docker build -f my-mood-tracker-server/database-service/docker/db_sql/Dockerfile . -t niksob/my-mood-tracker:db-sql-1.0.0
    docker build -f my-mood-tracker-server/database-service/docker/redis_db_cache/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0
'

# Push docker microservice images
#ssh -p 65001 root@80.242.58.161 '
#    docker push niksob/my-mood-tracker:config-service-1.0.0
#    docker push niksob/my-mood-tracker:database-service-1.0.0
#'