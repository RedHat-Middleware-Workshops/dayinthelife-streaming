# Day in the Streaming Life

## Event-Driven Workshop

This workshop is a series of hands-on modules which are designed to familiarize participants with event-driven concepts and give them a taste of using the new Red Hat Integration stack for building event-driven architectures.

The workshop will cover the following topics:

- How to connect enterprise on-premise software and container-native applications using Red Hat Integration
- Technologies to implement different event and messaging connectivity patterns
- What is Red Hat AMQ Streams (Apache Kafka) and the basics of its components
- How to run Red Hat AMQ Streams (Apache Kafka) on OpenShift, the enterprise Kubernetes
- Event-driven microservices benefits and patterns
- How to implement an event management bus using Red Hat Middleware

### Audience

#### Developers, Architects, Data Integrators

### Prerequisites

* Debezium
* AMQ Streams
* AMQ Online
* Camel K
* Fuse Online
* OpenShift 4.x

### Duration

Each module should be able to be completed in between 60-90 minutes plus 20 mins of slides introducing the content.

### Installation

Follow the [Installation Procedure](docs/install.md#installation) to setup a working environment.

## Workshop Modules

This workshop is broken into 4 time-boxed modules:

Module 1: Introduction to Event Driven Architecture<br/>
Module 2: Change Data Capture & Apache Kafka<br/>
Module 3: Cloud-Native Enterprise Integration Patterns using Camel K<br/>
Module 4: Event Sourcing and Complex Event Processing<br/>

This workshop could also be split up. All modules could be delivered on their own as standalone workshops, but we **strongly** recommend to deliver them in the suggested order.

### Background Story

The premise for our “Day in the Life” workshops is centered around a fictitious global conglomerate called International Inc.  Initially, International Inc. focused on forming a Digital Transformation strategy.  They began with an API-first approach to help facilitate their merger & acquisition activities.  The outcome of this approach was very successful, and helped them keep up with their competitors, deliver new business functionality faster, and make the merger & acquisition process seamless.  International Inc. is now interested in exploring new business opportunities in the field of space exploration.

International Inc. has identified an eco-friendly startup company who recently created a buzz among the younger population.  The startup, called “Fleur de lune”, focuses their core business on space agriculture.  Pollution has affected the earth's crops so much that fresh food has lost its taste.  In a brilliant movement from “International Inc” in their path to expansion, they acquired “Fleur de lune” to become a major player in the space agriculture business.

As part of the acquisition, International Inc.’s CTO met with the Fleur de lune DevOps team to discuss how they develop software.  The CTO was so amazed by their culture and practices that he wants to scale this out to the rest of International Inc.  He is particularly interested in Fleur de lune’s use of cloud-native integration technologies, including high-performant data streaming, event-driven architectures, and change data capture (CDC).  

As you’re part of International Inc.’s Development team, you have been tasked by the CTO to learn more about these cloud-native event-driven technologies and come up with solutions found in the following modules.

### Module One

Taste of event driven and why.
(Move http call to async msg calls)

Since the acquisition, the International Inc.’s Development team has been pressured to make the transition smooth quickly. They have decided to start with a small project “Shopify” where it notifies both internal departments and external partners when an order has been placed, through pure REST implementation to event driven architecture.

* Create a new Topic on AMQ Online
* Migrate the REST integration to a messaging broker
* Difference between API calls and event driven
* Fault tolerance

### Module Two

Sharing real time data/event from earth to the moon
(Legacy to Cloud Data sharing between cluster in a hybrid environment)

To expand Sales channels,  “Fleur de lune” no longer limit their market to the existing youngster customer base. “International Inc” has helped them expand their sales channel by taking orders from Major retailers, shopping malls and even local pharmacies. Orders are coming in from earth, they need to be also synced up to the ones received by the moon’s local e-commerce.

* Install Kafka (on Legacy)
* Deploy Kafka Connect with Debezium (on Legacy)
* Config Debezium (on Legacy)
* Config Kafka Connect (with everything pre-installed in the cloud)

### Module Three

Edge to Data Lake - Orchestrating and composing events

Utilizing the existing inter-planet transportation already established from “International Inc”, most of the operation cost for  “Fleur de lune” was the shipping cost between earth and moon.  Now, thanks to “International Inc”, they can minimize the cost of shipment by using their service. To provide better customer experience. Closely monitoring the harvest and harvest batch shipping schedule, make Fleur De Lune more proactive. And faster respond to any emergency that may impact their business.

* Connect IoT data (simulator) to AMQ Online  (Task 1)
* Bridge AMQ online events from IoT to Kafka using Camel-K and write to DB (for module four). Couple of EIP is introduced in this section. Splitter, WireTap. (Task 2)
* Introducing online caching for shipping. Connect to Data Grid to access shipping data.  (Task 3)

### Module Four

A meteor shower has just hit a couple of the devices on some marshmallow farms, resulting in the incorrect cost estimation. This needs to be corrected so we can request asking the right amount from the cargo ship!

* Disaster STRIKES!! Data recovery and replaying events (IoT Breakdown)
* Start Up simulator (Needs to check if we can write back to the same offset)
* Replay CEP

### TODO

Data Virt
