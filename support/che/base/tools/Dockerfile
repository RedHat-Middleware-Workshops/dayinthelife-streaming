FROM centos:8


RUN dnf update -y && \
dnf install -y wget && \
dnf install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
dnf install -y nodejs && \
dnf install -y maven && \
dnf clean all

ADD oc /usr/bin

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /projects

ADD entrypoint.sh /entrypoint.sh

CMD /entrypoint.sh tail -f /dev/null