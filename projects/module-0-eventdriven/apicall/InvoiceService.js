/*
kamel run InventoryService.js --name inventory-service -d camel-undertow -d camel-jackson -d camel-swagger-java --dev
*/
const Processor = Java.type("org.apache.camel.Processor");
const p = Java.extend(Processor);
const proc = new p(processInvoicing);

c = restConfiguration();
c.setComponent('undertow');
c.apiContextPath("/api-doc");
c.apiProperty("cors", "true");
c.apiProperty("api.title", "Invoice Service");
c.apiProperty("api.version", "1.0");
c.setPort('8080');

rest('/')
    .post('notify/order')
    .to('direct:notify');

from('direct:notify')
    .log('Invoicing Notified ${body}')
    .delay(5000).asyncDelayed()
    .process(proc)
    .to('log:info');

function processInvoicing(e) {
    var order = JSON.parse(
        String.fromCharCode.apply(String, e.getMessage().getBody())
    );
    var result = JSON.stringify({
        orderId: order.orderId,
        itemId: order.itemId,
        department: 'invoicing',
        datetime: Date.now(),
        amount: order.quantity * order.price,
        currency: 'USD',
        invoiceId: 'B-0' + (Math.floor(1000 + Math.random() * 9999))
    });
    e.getMessage().setBody(result);
}