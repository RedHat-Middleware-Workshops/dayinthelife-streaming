/*
kamel run InventoryEvents.js --name inventory-events -d camel-amqp -d camel-jackson --dev
*/
const Processor = Java.type("org.apache.camel.Processor");
const p = Java.extend(Processor);
const proc = new p(processInventory);

const ConnectionFactory = Java.type("org.apache.qpid.jms.JmsConnectionFactory");

cf = new ConnectionFactory();
cf.setRemoteURI('amqp://event-bus-amqp-0-svc.messaging.svc.cluster.local');

components.get('amqp')
    .setConnectionFactory(cf);

from('amqp:topic:notify/orders?exchangePattern=InOnly')
    .log('Inventory Notified ${body}')
    .unmarshal().json()
    .setHeader('reply-to').simple('${body[username]}')
    .marshal().json()
    .process(proc)
    .toD('amqp:queue:notifications/${headers.reply-to}?exchangePattern=InOnly')
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