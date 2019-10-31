/*
kamel run InventoryService.js --name inventory-service -d camel-undertow -d camel-jackson -d camel-swagger-java --dev
*/
const Processor = Java.type("org.apache.camel.Processor");
const p = Java.extend(Processor);
const proc = new p(processInventory);

c = restConfiguration();
c.setComponent('undertow');
c.apiContextPath("/api-doc");
c.apiProperty("cors", "true");
c.apiProperty("api.title", "Inventory Service");
c.apiProperty("api.version", "1.0");
c.setPort('8080');

rest('/')
    .post('notify/order')
    .to('direct:notify');

from('direct:notify')
    .log('Inventory Notified ${body}')
    .process(proc)
    .to('log:info');

function processInventory(e) {
    var order = JSON.parse(
        String.fromCharCode.apply(String, e.getMessage().getBody())
    );
    var result = JSON.stringify({
        orderId: order.orderId,
        itemId: order.itemId,
        department: 'inventory',
        datetime: Date.now(),
        quantity: order.quantity,
        flavor: order.orderItemName,
        inventoryId: Math.floor(100000 + Math.random() * 999999)
    });
    e.getMessage().setBody(result);
}