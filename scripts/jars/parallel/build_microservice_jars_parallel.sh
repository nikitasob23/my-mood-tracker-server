#!/bin/bash

# Build base modules
. scripts/jars/parallel/build_base_modules_parallel.sh

echo "Build microservices"
mvn -f config-service/pom.xml clean package -Dmaven.test.skip=true &
mvn -f database-service/pom.xml clean package -Dmaven.test.skip=true &
mvn -f authorization-service/pom.xml clean package -Dmaven.test.skip=true &
mvn -f gateway-service/pom.xml clean package -Dmaven.test.skip=true &
mvn -f mail-sender/pom.xml clean package -Dmaven.test.skip=true &
wait
