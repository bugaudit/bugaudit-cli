# BugAudit CLI
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.shib.bugaudit/bugaudit-cli/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.shib.bugaudit/bugaudit-cli)
[![Build Status](https://gitlab.com/bugaudit/bugaudit-cli/badges/master/pipeline.svg)](https://gitlab.com/bugaudit/bugaudit-cli/pipelines)

Command Line Interface for BugAudit

### How does it work?
Clone the source code you wish to scan and perform the following while inside the source directory.
Set the following environmental variables before starting the scan
##### Mandatory environment variable
```bash
#The Project ID or key in the issue tracker of your choice
export BUGAUDIT_PROJECT=TEST
#The issue type in which you want to track the bugs of your choice
export BUGAUDIT_ISSUETYPE="Security Bug"
#The name of the Issue tracker
export BUGAUDIT_TRACKER_NAME=Jira
#The URL/endpoint of the Issue tracker
export BUGAUDIT_TRACKER_ENDPOINT="https://jira.example.com"
#The API key of to communicate with Issue tracker (Not required if username and password are specified)
export BUGAUDIT_TRACKER_API_KEY="ApiTokenGoesHere"
#The username part of credentials to communicate with Issue tracker (Not required API token is specified)
export BUGAUDIT_TRACKER_USERNAME=user
#The password part of credentials to communicate with Issue tracker (Not required API token is specified)
export BUGAUDIT_TRACKER_PASSWORD=password
#The location of the config file where workflow transitions and other configurations are specified (defaults to bugaudit-config.json)
export BUGAUDIT_CONFIG="config/test-config.json"
```
##### Optional environmental variables
```bash
#The assignee to whom the issues have to be assigned to
export BUGAUDIT_ASSIGNEE="somebody@example.com"
#The users who are required to watch or subscribe to the created issues (Might not be available in all issue trackers)
export BUGAUDIT_SUBSCRIBERS=somedude@example.com,manager@example.com
#The language for which the scanner has to run for (BugAudit identifies the language automatically by default if not specified)
export BUGAUDIT_LANG=Ruby
#Performs a scan and mocks the issue tracker update (useful to see what will be updated or created)
export BUGAUDIT_TRACKER_READONLY=TRUE
#Performs a scan only with the specified tool/plugin
export BUGAUDIT_SCANNER_TOOL=BundlerAudit
#Performs a scan inside the specified subdirectory
export BUGAUDIT_SCANNER_DIR=test/code/path
#Uses the config specified while running brakeman scanner
export BUGAUDIT_SCANNER_BRAKEMAN_CONFIG="config/brakeman.yml"
#Uses the ignore file specified while running brakeman scanner
export BUGAUDIT_SCANNER_BRAKEMAN_IGNORE="config/brakeman.ignore"
#Any scanner specific config (Not required mostly)
export BUGAUDIT_SCANNER_CONFIG=bugaudit-scanner-config.json
```
Once the above is done, run the following in the environment
```bash
curl -s https://bugaudit.github.io/bugaudit-cli/bugaudit-script | bash
```
[Refer this](https://bugaudit.github.io/bugaudit-cli/bugaudit-config.json) for a sample config file