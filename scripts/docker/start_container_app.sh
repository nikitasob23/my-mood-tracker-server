#!/bin/sh
export JASYPT_SECRET=$(cat /run/secrets/jasypt_secret)

if [ -z "$1" ]; then
  echo "Error: No JAR file path provided."
  exit 1
else
  echo "Starting Java application at $1"
  # Launching a Java application using a script argument
  exec java -jar "$1"
fi