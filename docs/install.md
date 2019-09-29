# Installation

This page describes the installation of the Day In the Streaming Life Workshop from the latest sources from GitHub.

## Installing using Ansible

We provide an Ansible playbook to install all the required components and software for this workshop.

Installing with Ansible requires creating an inventory file with the variables for configuring the system. Example inventory files can be found in the `support/install/ansible/inventory` folder.

### Prerequisites

If you want to install locally, please make sure to update your Ansible version to atleast 2.5 and CLI for OCP 4. 

### Procedure to install dil-streaming

The recommended way to install the workshop is running the ansible playbook from the OpenShift4 workshop cluster bastion machine. This is the fastest way to run the installer as it's already running in the cluster closest to the master node.

1. Provision the *OpenShift 4 Workshop* image in RHPDS.  Remember to take note of the *GUID* with your cluster environment variable.

1. Via the CLI, login to your newly provisioned OCP cluster using the `oc` command.

1. Git Clone the *dayinthelife streaming* Workshop installation repository.

    ```bash
    git clone https://github.com/RedHatWorkshops/dayinthelife-streaming.git
    ```
1. Change folder to installation base.

    ```bash
    cd dayinthelife-streaming/support/install/ansible/
    ```

1. Run the Ansible playbook. if you are running in bastion, please use sudo to have the right permission.

    ```bash
    ansible-playbook -i inventory/inventory.example playbooks/openshift/install.yaml
    ```
    Bastion: 
    
    ```sudo bash
    ansible-playbook -i inventory/inventory.example playbooks/openshift/install.yaml
    ```

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

    ```sudo bash
    vi inventory/integreatly.example
    ```

