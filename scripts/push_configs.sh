#!/bin/bash

commit_message="$1"
cp /Users/nickworker/Documents/Repo/my-mood-tracker-server/database-service/src/main/resources/common-config/database_connection.yml /Users/nickworker/Documents/Repo/my-mood-tracker-config
git add .
git commit -m "$commit_message"
git push origin master