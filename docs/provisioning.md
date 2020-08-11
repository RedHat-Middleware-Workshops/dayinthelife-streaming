# Day in the Life - Event-driven workshop

## RHPDS Provisioning Guide

This document is intended to act as a reference guide for how to “order” and deploy the Event-Driven Architecture Workshop with Apache Kafka hands-on modules, available on-demand on Red Hat Product Demo System (RHPDS), outlining the process and steps needed in order to deliver these workshops at customers and events.

**NOTE: You should provision your event environments on the afternoon of the day prior to your event. If your event is Tuesday at 9 AM, make your provisioning request no later than 2 PM on Monday.**

### Red Hat Product Demo System (RHPDS)

RHPDS provides a self-service portal for provisioning demos and workshop clusters across the Red Hat portfolio using a CloudForms instance that fronts various systems and services such as Ravello and OpenShift. RHPDS is the designated channel for on-demand automated access to OpenShift clusters for delivering demos and workshops. RHPDS is available at https://rhpds.redhat.com

All Red Hat associates and most partners have access to RHPDS. If you have not used RHPDS before:

* Go to [OPENTLC](https://account.opentlc.com/)
* [Account Management](https://account.opentlc.com/)
* Activate Account
* Set Password

If you have forgotten your password, you can reset your password at [OPENTLC Account Management](https://account.opentlc.com/).

### Provisioning an OpenShift Cluster for a Workshop

The **DIL Streaming - Event-driven Workshop** item in the RHPDS catalog allows provisioning an OpenShift Cluster with a capacity for the specified number of users. The user provisioning the cluster is the cluster-admin and has full access to the entire OpenShift environment, including SSH access to the bastion host used to access the cluster at the OS level.

To provision a cluster:

1. Login to https://rhpds.redhat.com

1. Click on Services > Catalogs in RHPDS and then on the catalog item Workshops > DIL Streaming - Event-driven Workshop

1. Review the information, and then click Order

1. Provide the workshop cluster details and then click on the Submit button.

1. After submitting the request, the provisioning process will get started and will send you automated emails with status updates and connection details. The provisioning between 60 and 90 minutes. It might take longer for a bigger number of users.

**Once the provisioning is complete, you will receive an email with everything you need.** It will contain all of the needed console links, user passwords, admin passwords, links to share with attendees, and other information about the workshop.

At the end of the email you should find the important information regarding the workshop. It should look similar to the following:

    Day In the Life - Event-Driven Microservices Workshop provisioned for 25 user(s)

    User Registration: https://users-registration.apps.cluster-eda2-5728.eda2-5728.example.opentlc.com
    You should share this URL (or a shortlink for it) -- It is all they will need to get started!

    Solution Explorer:  
    https://tutorial-web-app-webapp.apps.cluster-eda2-5728.eda2-5728.example.opentlc.com 

    OpenShift Console: https://console-openshift-console.apps.cluster-eda2-5728.eda2-5728.example.opentlc.com

The only link you need to share with the attendees is the first one **User Registration** where each person should put their name and the access token to get an assigned user and password as well as to receive the link to the Solution Explorer page.

You can use the OpenShift Console information to login as a cluster-admin and check for user advance and cluster management.

**Be sure to save this information!** You may also consider making shorter URLs to the longer URL User Registration link, using bit.ly or goo.gl or other shorteners.
