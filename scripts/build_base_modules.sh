#!/bin/bash

# Build base modules
echo "\n\n\nStart building logger module"          && mvn -f logger/pom.xml clean package -Dmaven.test.skip=true          && echo "Logger module was build"
echo "\n\n\nStart building layer connector module" && mvn -f layer-connector/pom.xml clean package -Dmaven.test.skip=true && echo "Layer connector module was build"
echo "\n\n\nStart building domain module"          && mvn -f domain/pom.xml clean package -Dmaven.test.skip=true          && echo "Domain module was build"