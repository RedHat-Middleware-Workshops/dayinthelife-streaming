# Copyright (c) 2020 Red Hat, Inc.
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#   Red Hat, Inc. - initial API and implementation
FROM quay.io/eclipse/che-sidecar-kubernetes-tooling:1.0.4-a46d022

ENV KAMEL_VERSION 1.0.0-RC2

RUN curl -L https://github.com/apache/camel-k/releases/download/${KAMEL_VERSION}/camel-k-client-${KAMEL_VERSION}-linux-64bit.tar.gz | tar -C /usr/local/bin -xz \
    && chmod +x /usr/local/bin/kamel

ENV OC_CLIENT_VERSION 4.3.5

RUN curl -L https://mirror.openshift.com/pub/openshift-v4/clients/ocp/${OC_CLIENT_VERSION}/openshift-client-linux.tar.gz | tar -C /usr/local/bin -xz \
    && chmod +x /usr/local/bin/oc
