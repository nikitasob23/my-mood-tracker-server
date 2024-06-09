#!/bin/bash

echo "Build base modules"
mvn -f logger/pom.xml clean install -Dmaven.test.skip=true &
mvn -f layer-connector/pom.xml clean install -Dmaven.test.skip=true &
mvn -f domain/pom.xml clean install -Dmaven.test.skip=true &
wait