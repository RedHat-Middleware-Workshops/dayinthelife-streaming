# Installation

This page describes the installation of the Day In the Streaming Life Workshop from the latest sources from GitHub.

<!-- ## Pre-requisites

You will need a Red Hat Management Integration cluster (a.k.a. Integreatly/i8) to install this workshop on. You can order a vanilla provisioning from the Red Hat Product Demo System (RHPDS) following this [instructions](https://docs.google.com/document/d/1lSb481fCiec0aTlJAw8cRLn_AiQjNgbCZsqq6wWfdWE/edit). For this workshop there is no need to setup the Github Integration.

To install the Open Banking Workshop, you need to have a host machine with the latest stable release version of the OpenShift client tools. RHPDS provides a bastion machine to do this, we STRONGLY recommend to use that machine.

You'll want to know how to [fork](https://help.github.com/articles/fork-a-repo/) and [clone](https://help.github.com/articles/cloning-a-repository/) a Git repository, and how to [check out a branch](https://git-scm.com/docs/git-checkout#git-checkout-emgitcheckoutemltbranchgt).

The Open Banking Workshop can be installed using automated Ansible playbooks or following manual steps.

## Installing using Ansible

We provide an Ansible playbook to install all the required components and software for this workshop.

Installing with Ansible requires creating an inventory file with the variables for configuring the system. Example inventory files can be found in the `support/install/ansible/inventory` folder.

### Procedure to install Integr8ly

The recommended way to install the workshop is running the ansible playbook from the Integreatly cluster bastion machine. This is the fastest way to run the installer as it's already running in the cluster closest to the master node.

1. Login to the bastion machine following the email instructions.

    ```bash
    ssh -i /path/to/ocp_workshop.pem ec2-user@bastion.GUID.openshiftworkshop.com
    ```

    Remember to update the *GUID* with your cluster environment variable and replace the path to the downloaded PEM file.

1. Git Clone the Open Banking Workshop installation repository.

    ```bash
    git clone https://github.com/jbossdemocentral/openbanking-workshop.git
    ```

1. Change folder to installation base.

    ```bash
    cd openbanking-workshop/support/install/ansible/
    ```

1. Edit the inventory with the correct environment hostnames according to your environment *GUID*.

    ```bash
    vi inventory/integreatly.example
    ```

1. Replace `master1.GUID.internal` under `[master]` where *GUID* is your environment identifier. Replace the `ocp_domain` and the `ocp_apps_domain` with your environment *GUID*. Also make sure the `userno` aligns with the number of users you provisioned originally in the Integreatly workshop.

    ```yaml
    ...

    [master]
    master1.GUID.internal      <---------
    ...

    [workshop:vars]
    sso_project=sso
    gogs_project=gogs
    microcks_project=microcks
    apicurio_project=apicurio
    namespace=threescale
    backend_project=international
    configure_only=false
    ocp_domain=GUID.openshiftworkshop.com      <---------
    ocp_apps_domain=apps.GUID.openshiftworkshop.com      <---------
    usersno=101      <---------
    che=false

    ...

    ```

1. Become super user running the following command.

    ```bash
    sudo su
    ```

1. Run the Ansible playbook.

    ```bash
    ansible-playbook -i inventory/integreatly.example playbooks/openshift/integreatly-install.yml
    ```

It will a take a couple of minutes to install the Open Banking environment.

When installation is over, you can point the attendants to the Solution Explorer web page where they can start working on the walkthroughs

```bash
https://tutorial-web-app-webapp.apps.GUID.openshiftworkshop.com/
```

Don't forget to replace *GUID* with your environment value. -->