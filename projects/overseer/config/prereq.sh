#Create the pre req AMQ online first, make sure it's running (Better for user manual check)
#cd /projects/FleurDeLune/projects/overseer/config
#oc apply -f prereq.yaml
AMQHOST=$(oc get addressspace amq -o yaml | grep serviceHost | sed "s/serviceHost://g" | tr -d "[:blank:]")
sed "s/REPLACE/$AMQHOST/g" edge.properties.bak > edge.properties
oc create configmap edge-config  --from-file=edge.properties
kamel run EdgeSimulator.java

echo REMEMBER this AMQP endpoint -- [$AMQHOST]