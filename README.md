# Day in the Streaming Life 

## Event-Driven Workshop 


### Audience:

**Developers, Architects, Data Integrators**

### Prereqisites:

* Debezium
* AMQ Streams
* AMQ Online
* Camel K
* DataVirt on Fuse Online 
* Fuse Online
* GraphQL 
* 3Scale
* OpenShift 4.x

### Background: 

The premise for our “Day in the Life” workshops is centered around a fictitious global conglomerate called International Inc.  Initially, International Inc. focused on forming a Digital Transformation strategy.  They began with an API-first approach to help facilitate their merger & acquisition activities.  The outcome of this approach was very successful, and helped them keep up with their competitors, deliver new business functionality faster, and make the merger & acquisition process seamless.  International Inc. is now interested in exploring new business opportunities in the field of space exploration.

International Inc. has identified an eco-friendly startup company who recently created a buzz among the younger population.  The startup, called “Fleur de lune”, focuses their core business on space agriculture.  Pollution has affected the earth's crops so much that fresh food has lost its taste.  In a brilliant movement from “International Inc” in their path to expansion, they acquired “Fleur de lune” to become a major player in the space agriculture business. 

As part of the acquisition, International Inc.’s CTO met with the Fleur de lune DevOps team to discuss how they develop software.  The CTO was so amazed by their culture and practices that he wants to scale this out to the rest of International Inc.  He is particularly interested in Fleur de lune’s use of cloud-native integration technologies, including high-performant data streaming, event-driven architectures, and change data capture (CDC).  

As you’re part of International Inc.’s Development team, you have been tasked by the CTO to learn more about these cloud-native event-driven technologies and come up with solutions found in the following modules.

## Workshop Modules


### Module Zero:
Taste of event driven and why. 
(Move http call to async msg calls)

Since the acquisition, the International Inc.’s Development team has been pressured to make the transition smooth quickly. They have decided to start with a small project “Shopify” where it notifies both internal departments and external partners when an order has been placed, through pure REST implementation to event driven architecture. 

* Create a new Topic on AMQ Online
* Migrate the REST integration to a messaging broker
* Add another external partner for notification 

### Module One:
Sharing real time data/event from earth to the moon
(Legacy to Cloud Data sharing between cluster in a hybrid environment)

To expand Sales channels,  “Fleur de lune” no longer limit their market to the existing youngster customer base. “International Inc” has helped them expand their sales channel by taking orders from Major retailers, shopping malls and even local pharmacies. Orders are coming in from earth, they need to be also synced up to the ones received by the moon’s local e-commerce.



* Install Kafka (on Legacy)
* Deploy Kafka Connect with Debezium (on Legacy)
* Config Debezium (on Legacy)
* Config Kafka Connect (with everything pre-installed in the cloud)



### Module Two:
Processing complex and multiple source IoT events
(Complex event processing)

Utilizing the existing inter-planet transportation already established from “International Inc”, most of the operation cost for  “Fleur de lune” was the shipping cost between earth and moon.  Now, thanks to “International Inc”, they can minimize the cost of shipment by using their service. 


* Connect IoT data (simulator) to AMQ Online 
* Bridge AMQ online events from IoT to Kafka using Camel-K and write to DB (for module four) also sending it as cloud events to trigger KNative
* Time window base processing from Kafka using Streams API (functional programming)
* Sends off result to earth (display on UI) of the estimate space cargo-ship (Mirror Maker)


 
### Module Three:
A meteor shower has just hit a couple of the devices on some marshmallow farms, resulting in the incorrect count of the harvest inventory. This needs to be corrected so we can request asking the right amount from the cargo ship!

* Disaster STRIKES!! Data recovery and replaying events (IoT Breakdown)
* Start Up simulator (Needs to check if we can write back to the same offset)
* Replay CEP


### Module Four:
Monopoly Fine! Providing Data as a Service across domains
(Government regulation)

Congratulations!!!! The space marshmallow was a hit, it’s revenue goes up, shareholders are happy. BUT! Governments starts to notice the growth of the market and are paying attention to how low tax has become for Space products. They quickly place a new regulation which requires partial donation to the NGOs from the revenue to counteract Monopoly. 

* DV expose harvest data in DB
* Drools to calculate donation and write to DB or expose as API calculate on the fly
* Camel creates a gateway for internal users WEBUI (GraphQL) and also with REST
* Expose GraphQL using 3scale


### Module Five:
Applying business policies in Service Mesh
(This is why we need Business policy in Service Mesh level)

The new donation to NGO and the tax regulation caught International Inc by surprise. To allow higher revenue in the annual report for their stockholders. The CFO decided to introduce new tax strategy in International Inc, since Fleur de lune and International Inc are still separate legal entities, International Inc needs  Fleur de lune to create a chargeback mechanism, as they will be billing International Inc for all services, even data services for the sake of tax reduction. 

* Expose harvest info using Fuse Online
* Configure Policy using 3scale 
* Applying Policy to Service Mesh
