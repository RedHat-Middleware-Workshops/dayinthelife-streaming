/*
kamel run OrderServiceEvents.groovy --name order-service-events -d camel-amqp -d camel-undertow -d camel-jackson -d camel-swagger-java --dev
*/
camel {
    components {
        amqp {
            connectionFactory = new org.apache.qpid.jms.JmsConnectionFactory(
                new URI('amqp://event-bus-amqp-0-svc.messaging.svc.cluster.local')
            )
        }
    }
}

rest {
    configuration { 
        component 'undertow'
        apiContextPath '/api-docs'
        apiProperty 'cors', 'true'
        apiProperty 'api.title', 'Order API'
        apiProperty 'api.version', '1.0'
        enableCORS true
        port '8080'
    }
}

rest()
    .post("/place")
        .to("direct:placeorder")

from('direct:placeorder')
    .unmarshal().json()
    .log('placeorder--> ${body}')
    .marshal().json()
    .to('amqp:topic:notify/orders?exchangePattern=InOnly')
    .setHeader(Exchange.HTTP_RESPONSE_CODE).constant(202)
    .setBody(simple("Order Placed"))
    .to('log:info')