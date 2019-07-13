FROM alpine
LABEL maintainer="shibme"
RUN mkdir bugaudit-workspace
WORKDIR bugaudit-workspace
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
RUN mkdir /root/.ssh
COPY docker-git-config /root/.ssh/config
RUN chmod 400 /root/.ssh/config
ADD /target/bugaudit-runner.jar /bugaudit-workspace/bugaudit-runner.jar
ADD /bugaudit-runner /bugaudit-workspace/bugaudit-runner
RUN chmod +x /bugaudit-workspace/bugaudit-runner
ENTRYPOINT ["/bugaudit-workspace/bugaudit-runner"]