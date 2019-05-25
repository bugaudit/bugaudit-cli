#!/bin/bash
if [ ! -z $1 ]
then
  git clone https://gitlab-ci-token:${CI_JOB_TOKEN}@gitlab.com/${1}.git dependency-repo-to-build
  echo
  mvn -f dependency-repo-to-build/pom.xml clean install
  rm -rf dependency-repo-to-build
else
  echo "Please provide your GitLab repo as arugument in the form : org/repo"
fi
