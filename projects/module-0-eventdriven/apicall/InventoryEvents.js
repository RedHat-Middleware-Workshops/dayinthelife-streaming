/*
kamel run InventoryEvents.js --name inventory-events -d camel-amqp -d camel-jackson --configmap amqp-properties --dev
*/
const Processor = Java.type("org.apache.camel.Processor");
const p = Java.extend(Processor);
const proc = new p(processInventory);

from('amqp:topic:incomingorders?exchangePattern=InOnly')
    .log('Inventory Notified ${body}')
    .unmarshal().json()
    .setHeader('reply-to').simple('${body[username]}')
    .marshal().json()
    .delay(10000).asyncDelayed()
    .process(proc)
    .toD('amqp:queue:notifications?exchangePattern=InOnly')
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