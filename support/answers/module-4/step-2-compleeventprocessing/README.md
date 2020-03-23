# Kafka Streams Quarkus Kogito Project


The *alert* topic listens to incoming feeds of harvest count from edge device. The disaster monitor application will monitor all the input and sum up the total incoming counts, if the total harvest count is less then 150 every 10 sec, it will need to notify users in the *disaster* channel (topic)


****
edge (JSON : harvesevent) 

-> alert(kafka topic) 

-> OUR APP (if sum of harvest count > 150 every 10 sec) 

-> disaster=true/false(kafka topic)
****

## Running Local
### Install and run Kafka

Run zookeeper

```
bin/zookeeper-server-start.sh config/zookeeper.properties
```

Run kafka 

```
bin/kafka-server-start.sh config/server.properties
```
Create two topics needed

```
bin/kafka-topics.sh --create --zookeeper localhost:2181     --replication-factor 1 --partitions 1 --topic alert
bin/kafka-topics.sh --create --zookeeper localhost:2181     --replication-factor 1 --partitions 1 --topic disaster
```

### Start Application

Start the application

```
mvn clean package quarkus:dev
```

In another CMD console start a producer to send harvest data (***HARVEST SENDER***)

```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic alert
```

In another CMD console start a producer to recieve disaster alert

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic disaster
```


### Send In data 

In ***HARVEST SENDER*** send in the following data

```
{"batchtime":1582661010246, "batchcnt":6, "farmid":101}

{"batchtime":1582661015283, "batchcnt":78, "farmid":302}

{"batchtime":1582661020246, "batchcnt":57, "farmid":645}
```

If batch count sent into alert topic, add up more than 150, no disaster.

Should get something similar as follows. 

```
{"totalCnt":702,"disaster":false}
{"totalCnt":156,"disaster":false}
{"totalCnt":78,"disaster":true}
{"totalCnt":78,"disaster":true}
```

## Deploying to OpenShift

### Install AMQ Streams

```
oc new-project streams
```

- Install AMQ Streams Operator
- create kafka cluster
- create two kafka topics, alert and dis


### Build and deploy to OpenShift

Login to OpenShift with the CLI tool

```
oc new-project mydemo
oc new-build --binary --name=cep-kogito -l app=cep-kogito
oc patch bc/cep-kogito -p "{\"spec\":{\"strategy\":{\"dockerStrategy\":{\"dockerfilePath\":\"src/main/docker/Dockerfile.jvm\"}}}}"
oc start-build cep-kogito --from-dir=. --follow
```

Update the configuration in application.properties for Kafka server URL: 
There are two ways, with config map or without. 

```
oc create -f support/cm.yaml 
oc patch bc/cep-kogito -p "{\"spec\":{\"source\": {\"configMaps\": [{\"configMap\": {\"name\": \"cep-kogito\"}}]}}}"
oc new-app --image-stream=cep-kogito
oc patch dc/cep-kogito -p "{\"spec\": {\"template\": {\"spec\": {\"containers\": [{\"name\": \"cep-kogito\",\"env\": [{\"name\": \"quarkus.kafka-streams.bootstrap-servers\",\"valueFrom\": {\"configMapKeyRef\": {\"name\": \"cep-kogito\",\"key\": \"bootstrap-servers\",\"optional\": false}}},{\"name\": \"quarkus.kafka-streams.application-server\",\"valueFrom\": {\"configMapKeyRef\": {\"name\": \"cep-kogito\",\"key\": \"application-servers\",\"optional\": false}}}]}]}}}}"
```


or you can simply do this:

```
oc new-app --image-stream=cep-kogito \
 -e quarkus.kafka-streams.bootstrap-servers=my-cluster-kafka-bootstrap.streams.svc:9092 \
 -e quarkus.kafka-streams.application-server=my-cluster-kafka-bootstrap.streams.svc:9090 
```