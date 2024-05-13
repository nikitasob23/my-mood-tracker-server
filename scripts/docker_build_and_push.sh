#!/bin/bash

# Build microservice's jars
. scripts/build_microservice_jars.sh

# Build docker microservice images
echo "\n\n\nStart building docker config service"        && docker build -f config-service/Dockerfile . -t niksob/my-mood-tracker:config-service-1.0.0               && echo "Docker config service was build"
#echo "\n\n\nStart building docker database service"      && docker build -f database-service/Dockerfile . -t niksob/my-mood-tracker:database-service-1.0.0           && echo "Docker database service was build"
#echo "\n\n\nStart building docker authorization service" && docker build -f authorization-service/Dockerfile . -t niksob/my-mood-tracker:authorization-service-1.0.0 && echo "Docker authorization service was build"
#echo "\n\n\nStart building docker gateway service"       && docker build -f gateway-service/Dockerfile . -t niksob/my-mood-tracker:gateway-service-1.0.0             && echo "Docker gateway service was build"
#echo "\n\n\nStart building docker mail sender"           && docker build -f mail-sender/Dockerfile . -t niksob/my-mood-tracker:mail-sender-1.0.0                     && echo "Docker mail sender was build"

# Push docker microservice images
#echo "\n\n\nStart pushing docker config service"        && docker push niksob/my-mood-tracker:config-service-1.0.0        && echo "Docker config service was push"
#echo "\n\n\nStart pushing docker database service"      && docker push niksob/my-mood-tracker:database-service-1.0.0      && echo "Docker database service was push"
#echo "\n\n\nStart pushing docker authorization service" && docker push niksob/my-mood-tracker:authorization-service-1.0.0 && echo "Docker authorization service was push"
#echo "\n\n\nStart pushing docker gateway service"       && docker push niksob/my-mood-tracker:gateway-service-1.0.0       && echo "Docker gateway service was push"
#echo "\n\n\nStart pushing docker mail sender"           && docker push niksob/my-mood-tracker:mail-sender-1.0.0           && echo "Docker mail sender was push"