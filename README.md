# BugAudit CLI
[![Download](https://api.bintray.com/packages/bugaudit/maven/bugaudit-cli/images/download.svg)](https://bintray.com/bugaudit/maven/bugaudit-cli/_latestVersion)
[![Build Status](https://gitlab.com/bugaudit/bugaudit-cli/badges/master/pipeline.svg)](https://gitlab.com/bugaudit/bugaudit-cli/pipelines)

Command Line Interface for BugAudit

### Requirements
Install the latest stable version of docker client

### What does it do?
Provides a docker container with all platform dependencies bundled to run BugAudit

### Where to begin?
Run the following in your terminal to get into the container.
```bash
docker run -ti bugaudit-platform
```
Copy or mount your source code into `/bugaudit-worspace/` directory inside the container and then just run the following.
```bash
bugaudit
```
[Refer this](https://bugaudit.github.io/bugaudit/config.json) for a sample config file