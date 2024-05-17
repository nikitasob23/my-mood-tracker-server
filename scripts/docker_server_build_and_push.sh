#!/bin/bash

# Build microservice's jars
. scripts/build_microservice_jars.sh

. scripts/copy_jar_and_dockerfile_on_server.sh

# Build docker microservice images
ssh -p 65001 root@80.242.58.161 '
#    echo "\n\nStart building database sql docker file"          && docker build -f my-mood-tracker-server/database-service/docker/db_sql/Dockerfile . -t niksob/my-mood-tracker:db-sql-1.0.0
    echo "\n\nStart building database cache docker file"        && docker build -f my-mood-tracker-server/database-service/docker/redis_db_cache/Dockerfile . -t niksob/my-mood-tracker:db-redis-cache-1.0.0
    echo "\n\nStart building authorization cache docker file"   && docker build -f my-mood-tracker-server/authorization-service/docker/redis_cache/Dockerfile . -t niksob/my-mood-tracker:auth-redis-cache-1.0.0
    echo "\n\nStart building config service docker file"        && docker build -f my-mood-tracker-server/config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0
    echo "\n\nStart building database service docker file"      && docker build -f my-mood-tracker-server/database-service/docker/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0
    echo "\n\nStart building authorization service docker file" && docker build -f my-mood-tracker-server/authorization-service/docker/Dockerfile . -t niksob/my-mood-tracker:authorization-service-1.0.0
    echo "\n\nStart building mail sender docker file"           && docker build -f my-mood-tracker-server/mail-sender/Dockerfile . -t niksob/my-mood-tracker:mail-sender-1.0.0
    echo "\n\nStart building gateway service docker file"       && docker build -f my-mood-tracker-server/gateway-service/Dockerfile . -t niksob/my-mood-tracker:gateway-service-1.0.0
'

 Push docker microservice images
ssh -p 65001 root@80.242.58.161 '
    echo "\n\nStart pushing database sql docker file"          && docker push niksob/my-mood-tracker:db-sql-1.0.0
    echo "\n\nStart pushing database cache docker file"        && docker push my-mood-tracker:db-redis-cache-1.0.0
    echo "\n\nStart pushing authorization cache docker file"   && docker push my-mood-tracker:auth-redis-cache-1.0.0
    echo "\n\nStart pushing config service docker file"        && docker push niksob/my-mood-tracker:config-service-1.0.0
    echo "\n\nStart pushing database service docker file"      && docker push niksob/my-mood-tracker:database-service-1.0.0
    echo "\n\nStart pushing authorization service docker file" && docker push niksob/my-mood-tracker:authorization-service-1.0.0
    echo "\n\nStart pushing mail sender docker file"           && docker push niksob/my-mood-tracker:mail-sender-1.0.0
    echo "\n\nStart pushing gateway service docker file"           && docker push niksob/my-mood-tracker:gateway-service-1.0.0
'