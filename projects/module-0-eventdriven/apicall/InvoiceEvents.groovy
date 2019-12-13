/*
kamel run InvoiceEvents.groovy --name invoice-events -d camel-jackson -d camel-amqp --configmap amqp-properties --dev
*/
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

from('amqp:topic:incomingorders?exchangePattern=InOnly')
    .log('Invoicing Notified ${body}')
    .unmarshal().json()
    .delay(30000).asyncDelayed()
    .process('processInvoice')
    .marshal().json()
    .convertBodyTo(String.class)
    .log('H:${headers}')
    .toD('amqp:queue:notifications?exchangePattern=InOnly')
    .to('log:info')