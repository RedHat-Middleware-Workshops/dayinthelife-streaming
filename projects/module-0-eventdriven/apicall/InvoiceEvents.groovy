/*
kamel run --name invoice-events -d camel-jackson -d camel-amqp InvoiceEvents.groovy --dev
*/

camel {
    components {
        amqp {
            connectionFactory new org.apache.qpid.jms.JmsConnectionFactory(new URI('amqp://event-bus-amqp-0-svc.messaging.svc.cluster.local'))
        }
    }
}

beans {
    processInvoice = processor {
        it.out.body = [
            orderId: it.in.body.orderId,
            itemId: it.in.body.itemId,
            department: 'invoicing',
            datetime: System.currentTimeMillis(),
            amount: (it.in.body.quantity * it.in.body.price),
            currency: 'USD',
            invoiceId: 'B-0' + (Math.floor(1000 + Math.random() * 9999))
        ]
        it.out.headers['reply-to'] = it.in.body.username
    }
}

from('amqp:topic:notify/orders?exchangePattern=InOnly')
    .log('Invoicing Notified ${body}')
    .unmarshal().json()
    .delay(5000).asyncDelayed()
    .process('processInvoice')
    .marshal().json()
    .convertBodyTo(String.class)
    .log('H:${headers}')
    .toD('amqp:queue:notifications/${headers.reply-to}?exchangePattern=InOnly')
    .to('log:info')