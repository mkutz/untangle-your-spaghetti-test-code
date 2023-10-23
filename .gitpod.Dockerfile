FROM gitpod/workspace-full:2022-10-25-06-57-58

SHELL ["/bin/bash", "-c"]
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 21-tem < /dev/null \
