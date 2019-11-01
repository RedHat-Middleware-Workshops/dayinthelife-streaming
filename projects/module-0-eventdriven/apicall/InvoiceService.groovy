/*
kamel run InvoiceService.groovy --name invoice-service -d camel-undertow -d camel-jackson -d camel-swagger-java --dev
*/
rest {
    configuration { 
        component 'undertow'
        apiContextPath '/api-docs'
        port '8080'
    }
}

beans {
    processInvoice = processor {
        it.out.body = [
            orderId: it.in.body.orderId,
            itemId: it.in.body.itemId,
            department: 'invoicing',
            datetime: new Date(),
            amount: (it.in.body.quantity * it.in.body.price),
            currency: 'USD',
            invoiceId: 'B-0' + (Math.floor(1000 + Math.random() * 9999))
        ]
    }
}

rest().post("/notify/order")
    .route()
        .to('direct:notify')

from('direct:notify')
    .unmarshal().json()
    .log('Inventory Notified ${body}')
    .delay(5000).asyncDelayed()
    .process('processInvoice')
    .marshal().json()
    .to('log:info')