ARG BUGAUDIT_VERSION
FROM bugaudit/bugaudit-platform:$BUGAUDIT_VERSION
LABEL maintainer="shibme"
WORKDIR /bugaudit-workspace
ADD /target/bugaudit-runner.jar /bugaudit-executables/bugaudit-runner.jar
ADD /bugaudit-runner /bugaudit-executables/bugaudit-runner
RUN chmod +x /bugaudit-executables/bugaudit-runner
RUN ln -s /bugaudit-executables/bugaudit-runner /bin/bugaudit-runner
ADD /bugaudit-command /bugaudit-executables/bugaudit-command
RUN chmod +x /bugaudit-executables/bugaudit-command
RUN ln -s /bugaudit-executables/bugaudit-command /bin/bugaudit
CMD ["bugaudit-runner"]