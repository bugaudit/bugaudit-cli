#!/bin/bash

#Set the environmental variables below before beginning
#export BUGAUDIT_TRACKER_ENDPOINT="http://jira.example.com"
#export BUGAUDIT_TRACKER_API_KEY="xxxxxyyyyyzzzzz" -- Not required if username and password are provided
#export BUGAUDIT_TRACKER_USERNAME="sampleusername" -- Not required if API Key is provided
#export BUGAUDIT_TRACKER_PASSWORD="samplepassword" -- Not required if API Key is provided
#export GIT_URL="https://github.com/test/test.git"
#export BUGAUDIT_CLI_TOKEN="BugAuditCLIToken"
#export BUGAUDIT_TRACKER_CONFIG="config/file/path"

GITHUB_TOKEN_PARAMETER=""
if [ ! -z "$BUGAUDIT_CLI_TOKEN" ]; then
   GITHUB_TOKEN_PARAMETER="?access_token=$BUGAUDIT_CLI_TOKEN"
fi

#Setting Repo and API URL
TOOL_REPO="shibme/bugaudit-cli"
API_URL="https://api.github.com/repos/$TOOL_REPO"

#Saving latest release info to assets.json
curl "$API_URL/releases/latest$GITHUB_TOKEN_PARAMETER" > assets.json

#Using jq to get Asset ID and File Name
ASSET_ID=$(jq -r '.assets[1].id' assets.json)
ASSET_NAME=$(jq -r '.assets[1].name' assets.json)

#Cleaning up locally stored Asset File and assets.json file
rm assets.json
rm -f $ASSET_NAME

#Downloading file from GitHub releases
curl -O -J -L -H "Accept: application/octet-stream" "$API_URL/releases/assets/$ASSET_ID$GITHUB_TOKEN_PARAMETER"

java -jar $ASSET_NAME