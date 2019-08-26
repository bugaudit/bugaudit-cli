FROM alpine:3.10.2
LABEL maintainer="shibme"
RUN apk update && apk upgrade
RUN apk add curl wget bash
RUN apk add openssh-client
RUN apk add git
RUN apk add openjdk8-jre
RUN apk add ruby ruby-io-console ruby-bundler ruby-json
RUN gem install rdoc --no-document
RUN gem install bundler:1.17.1
RUN gem install bundler
RUN gem install brakeman
RUN gem install bundler-audit
RUN apk add npm
RUN npm install -g retire
WORKDIR /bugaudit-tools
ADD https://dl.bintray.com/jeremy-long/owasp/dependency-check-5.2.1-release.zip /bugaudit-tools/dependency-check.zip
RUN unzip dependency-check.zip
RUN rm dependency-check.zip
RUN ln -s /bugaudit-tools/dependency-check/bin/dependency-check.sh /bin/dependency-check
WORKDIR /bugaudit-workspace
RUN mkdir /root/.ssh
COPY docker-git-config /root/.ssh/config
RUN chmod 400 /root/.ssh/config
ADD /target/bugaudit-runner.jar /bugaudit-executables/bugaudit-runner.jar
ADD /bugaudit-runner /bugaudit-executables/bugaudit-runner
RUN chmod +x /bugaudit-workspace/bugaudit-runner
RUN ln -s /bugaudit-executables/bugaudit-runner /bin/bugaudit-runner
ADD /bugaudit-command /bugaudit-executables/bugaudit-command
RUN chmod +x /bugaudit-executables/bugaudit-command
RUN ln -s /bugaudit-executables/bugaudit-command /bin/bugaudit
RUN dependency-check -s /tmp/
RUN rm dependency-check-report.html
RUN bundle audit update