#!/bin/bash

# Copy jars
#scp -P 65001 config-service/target/config-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/config-service/config-service-0.0.1.jar
#scp -P 65001 database-service/target/database-service-0.0.1-SNAPSHOT.jar root@80.242.58.161:/root/my-mood-tracker-server/database-service/database-service-0.0.1.jar

# Copy Dockerfiles
scp -P 65001 config-service/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/config-service/
scp -P 65001 database-service/docker/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/
scp -P 65001 database-service/docker/db_sql/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/db_sql/
scp -P 65001 database-service/docker/redis_db_cache/Dockerfile root@80.242.58.161:/root/my-mood-tracker-server/database-service/docker/redis_db_cache/