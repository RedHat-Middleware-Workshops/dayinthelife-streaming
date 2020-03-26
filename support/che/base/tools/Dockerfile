FROM centos:8


RUN dnf update -y && \
dnf install -y wget && \
dnf install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
dnf install -y nodejs && \
dnf install -y maven && \
dnf clean all

# Define working directory.
ADD oc /usr/bin

# Set environment variables.
ENV HOME /projects
RUN alternatives --set java /usr/lib/jvm/java-11-openjdk-11.0.6.10-0.el8_1.x86_64/bin/java

WORKDIR /projects

ADD entrypoint.sh /entrypoint.sh
#CMD "$@"
CMD /entrypoint.sh tail -f /dev/null
