#!/bin/bash

#Set the environmental variables below before beginning
#export GITHUB_ACCESS_TOKEN=GitHubAccessTokenGoesHere
#export BUGAUDIT_CONFIG=bugaudit-config.json
#export BUGAUDIT_PROJECT=TEST
#export BUGAUDIT_ISSUETYPE=Bug
#export BUGAUDIT_ASSIGNEE=testuser@example.com
#export BUGAUDIT_SUBSCRIBERS=coolguy@test.com,tester@example.com
#export BUGAUDIT_TRACKER_READONLY=TRUE
#export BUGAUDIT_TRACKER_NAME=Jira
#export BUGAUDIT_TRACKER_ENDPOINT=https://example.jira.com
#export BUGAUDIT_TRACKER_USERNAME=user -- Not required if API Key is provided
#export BUGAUDIT_TRACKER_PASSWORD=password -- Not required if API Key is provided
#export BUGAUDIT_TRACKER_API_KEY=XxyYAbcZ -- Not required if username and password are provided
#export BUGAUDIT_SCANNER_PARSERONLY=TRUE
#export BUGAUDIT_SCANNER_TOOL=BundlerAudit
#export BUGAUDIT_SCANNER_DIR=test/code/path
#export BUGAUDIT_SCANNER_CONFIG=bugaudit-scanner-config.json
#export BUGAUDIT_SCANNER_BRAKEMAN_CONFIG="config/brakeman.yml"
#export BUGAUDIT_SCANNER_BRAKEMAN_IGNORE="config/brakeman.ignore"


if [ ! -z "$BUGAUDIT_SCANNER_DIR" ]; then
   cd $BUGAUDIT_SCANNER_DIR
fi

GITHUB_TOKEN_PARAMETER=""
if [ ! -z "$GITHUB_ACCESS_TOKEN" ]; then
   GITHUB_TOKEN_PARAMETER="?access_token=$GITHUB_ACCESS_TOKEN"
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