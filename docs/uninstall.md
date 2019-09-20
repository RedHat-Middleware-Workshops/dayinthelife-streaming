# Uninstallation

This page describes the uninstallation of the Day In the Streaming Life Workshop from the latest sources from GitHub.

## Uninstalling using Ansible

We provide an Ansible playbook to uninstall all the required components and software for this workshop.

Uninstalling scripts can be found in the `support/uninstall/ansible` folder.

### Procedure to uninstall dil-streaming

The recommended way to uninstall the workshop is running the ansible playbook using `oc login` via the CLI. This is the fastest way to run the installer as it's already running in the cluster closest to the master node.


1. Via the CLI, login to your OCP cluster using the `oc login` command.

1. Change folder to the uninstall base.

    ```bash
    cd dayinthelife-streaming/support/uninstall/ansible/
    ```

1. Run the Ansible playbook. if you are running in bastion, please use sudo to have the right permission.

    ```bash
    ansible-playbook -i inventory/inventory.example playbooks/openshift/uninstall.yaml
    ```
    Bastion:

    ```sudo bash
    ansible-playbook -i inventory/inventory.example playbooks/openshift/uninstall.yaml
    ```
