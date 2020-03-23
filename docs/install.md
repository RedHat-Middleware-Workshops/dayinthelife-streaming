# Installation

This page describes the installation of the Day In the Streaming Life Workshop (DIL Streaming) from the latest sources from GitHub.

## Installing using Ansible

We provide an Ansible playbook to install all the required components and software for this workshop.

Installing with Ansible requires creating an inventory file with the variables for configuring the system. Example inventory files can be found in the `support/install/ansible/inventory` folder.

### Prerequisites

If you want to install locally, please make sure to update your Ansible version to at least 2.6 and the CLI tool for OCP 4.

* Ansible 2.9.2 +
* OpenShift command-line interface (CLI) 4.3.x +
* OpenShift Container Platform 4.3+

### Procedure to install dayinthelife-streaming

The recommended way to install the workshop is running the ansible playbook from the OpenShift4.3 workshop cluster bastion machine. This is the fastest way to run the installer as it's already running in the cluster closest to the master node.

1. Provision the *OpenShift 4.3 Workshop* service from Red Hat Product Demo System (RHPDS).  Remember to take note of the *GUID* with your cluster environment variable.

1. Via the CLI, verify you are logged in as cluster admin to your newly provisioned OCP cluster using the `oc` command.

1. Git Clone the *dayinthelife streaming* Workshop installation repository.

    ```bash
    git clone https://github.com/RedHatWorkshops/dayinthelife-streaming.git
    ```

1. Change folder to installation base.

    ```bash
    cd dayinthelife-streaming/support/install/ansible/
    ```

1. Run the Ansible playbook. The fool-proof way is to run it from the bastion server.  Otherwise, if you are confident your local Ansible / Python libraries are the latest and greatest, you can try running the playbook from your own machine (at your own risk).

    ```bash
    bash -c "ansible-playbook -i inventory/inventory.example playbooks/openshift/install.yaml"
    ```

Note: IGNORE the error if the installer has an error at this step 
TASK [provision_che : Create workspace for user3 from devfile] ***************************************************************
fatal: [localhost]: FAILED! => {"changed": false, "connection": "close", "content": "{}", "content_type": "application/json", "date": "Mon, 23 Mar 2020 20:51:55 GMT", "elapsed": 0, "json": {}, "msg": "Status code was 500 and not [201]: HTTP Error 500: ", "redirected": false, "set_cookie": "0f443a2a27de429b7964dbc8f3c18ef4=e54147ad4656ff99341649b8ea63e50b; path=/; HttpOnly", "status": 500, "transfer_encoding": "chunked", "url": "  <some url", "vary": "accept-encoding"}
...ignoring
