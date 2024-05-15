#!/bin/bash

# Copying docker-compose file
scp -P 65001 /Users/nickworker/Documents/Repo/my-mood-tracker-server/compose-env.yml root@80.242.58.161:/root/my-mood-tracker-server

# Starting Docker containers
ssh -p 65001 root@80.242.58.161 'docker-compose -f my-mood-tracker-server/compose-env.yml up'