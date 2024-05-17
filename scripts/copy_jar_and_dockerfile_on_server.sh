#!/bin/bash

# Copy jars
echo "\nStart copying service's jar files"
scp -P 65001 config-service/target/config-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/config-service/config-service-0.0.1.jar &
scp -P 65001 database-service/target/database-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/database-service/database-service-0.0.1.jar &
scp -P 65001 authorization-service/target/authorization-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/authorization-service/authorization-service-0.0.1.jar &
scp -P 65001 mail-sender/target/mail-sender-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/mail-sender/mail-sender-0.0.1.jar &
scp -P 65001 gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/gateway-service/gateway-service-0.0.1.jar &
wait
echo "All JAR files have been copied successfully."

# Copy Dockerfiles
echo "\n\nStart coping service's docker files"
#scp -P 65001 database-service/docker/db_sql/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/db_sql/ &
scp -P 65001 database-service/docker/redis_db_cache/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/redis_db_cache/ &
scp -P 65001 authorization-service/docker/redis_cache/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/authorization-service/docker/redis_cache/ &
scp -P 65001 config-service/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/config-service/ &
scp -P 65001 database-service/docker/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/ &
scp -P 65001 authorization-service/docker/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/authorization-service/docker/ &
scp -P 65001 mail-sender/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/mail-sender/ &
scp -P 65001 gateway-service/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/gateway-service/ &
wait
echo "All DOCKER files have been copied successfully."