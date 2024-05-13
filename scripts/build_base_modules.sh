#!/bin/bash

# Build base modules
echo "\n\n\nStart building logger module"          && mvn -f logger/pom.xml install -DskipTests          && echo "Logger module was build"
echo "\n\n\nStart building layer connector module" && mvn -f layer-connector/pom.xml install -DskipTests && echo "Layer connector module was build"
echo "\n\n\nStart building domain module"          && mvn -f domain/pom.xml install -DskipTests          && echo "Domain module was build"