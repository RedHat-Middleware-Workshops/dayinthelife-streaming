FROM registry.redhat.io/amq7/amq-streams-kafka-25-rhel7:1.5.0
USER root:root
COPY ./plugins/ /opt/kafka/plugins/
COPY ./apicurio/ /opt/kafka/external_libs/apicurio/
RUN for d in /opt/kafka/plugins/*/; do ln -snf /opt/kafka/external_libs/apicurio/* $d; done
USER kafka:kafka