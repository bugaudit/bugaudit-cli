FROM bugaudit/bugaudit-environment
LABEL maintainer="shibme"
RUN mkdir /root/.ssh
COPY docker-git-config /root/.ssh/config
RUN chmod 400 /root/.ssh/config
ADD /target/bugaudit-runner.jar /bugaudit-workspace/bugaudit-runner.jar
ADD /bugaudit-runner /bugaudit-workspace/bugaudit-runner
RUN chmod +x /bugaudit-workspace/bugaudit-runner
CMD ["/bugaudit-workspace/bugaudit-runner"]