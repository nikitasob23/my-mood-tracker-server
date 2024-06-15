#!/bin/bash

# Build base modules
. scripts/jars/build_base_modules.sh

# Build microservices
echo "\n\n\nStart building config service"        && mvn -f config-service/pom.xml clean package -Dmaven.test.skip=true        && echo "Config service was build"
echo "\n\n\nStart building database service"      && mvn -f database-service/pom.xml clean package -Dmaven.test.skip=true      && echo "Database service was build"
echo "\n\n\nStart building authorization service" && mvn -f authorization-service/pom.xml clean package -Dmaven.test.skip=true && echo "Authorization service was build"
echo "\n\n\nStart building gateway service"       && mvn -f gateway-service/pom.xml clean package -Dmaven.test.skip=true       && echo "Gateway service was build"
echo "\n\n\nStart building mail service"          && mvn -f mail-sender/pom.xml clean package -Dmaven.test.skip=true           && echo "Mail sender service was build"
