FROM registry.redhat.io/codeready-workspaces/plugin-java11-rhel8:2.15 AS kafkacat
USER root
RUN dnf -y install gcc which gcc-c++ wget make git cmake
ENV KAFKACAT_VERSION=1.7.0
RUN dnf -y install cyrus-sasl-devel libcurl-devel
RUN cd /tmp && git clone https://github.com/edenhill/kcat -b $KAFKACAT_VERSION --single-branch && \
    cd kcat && \
    ./bootstrap.sh

FROM registry.redhat.io/codeready-workspaces/plugin-java11-rhel8:2.15

ENV CAMELK_VERSION=1.6.7
ENV SKUPPER_VERSION=1.0.2
ENV RHOAS_VERSION=0.43.0
ENV TKN_VERSION=0.23.1
ENV KN_VERSION=1.1.0

USER root

# Install skupper
RUN wget https://github.com/skupperproject/skupper/releases/download/${SKUPPER_VERSION}/skupper-cli-${SKUPPER_VERSION}-linux-amd64.tgz \
    -O - | tar -xz -C /usr/local/bin/

# Install kamel
RUN wget https://mirror.openshift.com/pub/openshift-v4/clients/camel-k/${CAMELK_VERSION}/camel-k-client-${CAMELK_VERSION}-linux-64bit.tar.gz \
    -O - | tar -xz -C /usr/local/bin/

# Install rhoas
RUN wget https://github.com/redhat-developer/app-services-cli/releases/download/v${RHOAS_VERSION}/rhoas_${RHOAS_VERSION}_linux_amd64.tar.gz \
    -O - | tar -xz --strip 1 -C /usr/local/bin rhoas_${RHOAS_VERSION}_linux_amd64/rhoas

# Install Knative
RUN wget https://mirror.openshift.com/pub/openshift-v4/clients/serverless/${KN_VERSION}/kn-linux-amd64.tar.gz \
    -O - | tar -xz -C /usr/local/bin

# Install Tekton
RUN wget https://mirror.openshift.com/pub/openshift-v4/clients/pipeline/${TKN_VERSION}/tkn-linux-amd64-${TKN_VERSION}.tar.gz \
    -O - | tar -xz -C /usr/local/bin

# Install node
RUN dnf install -y nodejs && \
    dnf install -y jq && \
    dnf clean all

# Kafkacat from build
COPY --from=kafkacat /tmp/kcat/kcat /usr/local/bin/kafkacat
COPY --from=kafkacat /tmp/kcat/kcat /usr/local/bin/kcat

USER jboss

WORKDIR /projects
CMD tail -f /dev/null
