/* 
kamel run --name=inventory-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 InventoryService.java
kamel run --name=inventory-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d camel-ahc-ws -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 InventoryService.java --dev
kamel run --name=inventory-service -d camel-jackson -d camel-ahc-ws -d camel-amqp  InventoryService.java
*/
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.amqp.AMQPConnectionDetails;
import org.apache.camel.component.jackson.JacksonDataFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class InventoryService extends RouteBuilder {

    String BROKER_URL = "amqp://messaging-7rm3r5j1m3.workshop-operators.svc"+":5672"+"?amqp.saslMechanisms=PLAIN";
    String USERNAME = "user";
    String PWD = "enmasse";
    
    @Override
    public void configure() throws Exception {
        
        AMQPConnectionDetails amqpDetail = new AMQPConnectionDetails(BROKER_URL,USERNAME,PWD,false);
        getContext().getRegistry().bind("amqpDetail",AMQPConnectionDetails.class,amqpDetail);
         
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Map.class);
        JacksonDataFormat invDataFormat = new JacksonDataFormat();
        invDataFormat.setUnmarshalType(InventoryNotification.class);

        
        from("amqp:topic:incomingorders?subscriptionDurable=false")
            .unmarshal(jacksonDataFormat)
            .bean(InventoryNotification.class, "getInventoryNotification(${body['orderId']},${body['itemId']},${body['quantity']} )")
            .marshal(invDataFormat)
            .convertBodyTo(String.class)
            .log("Inventory Notified ${body}")
            .to("ahc-ws://dilii-ui:8181/echo")
            ;
    }


    private static class InventoryNotification {
        private Integer orderId;
        private Integer itemId;
        private Integer quantity;
        private String department;
        private Date datetime;

        public static InventoryNotification getInventoryNotification(Integer orderId, Integer itemId, Integer quantity ){
            InventoryNotification invenNotification  = new InventoryNotification();
            invenNotification.setOrderId(orderId);
            invenNotification.setItemId(itemId);
            invenNotification.setQuantity(quantity);
            invenNotification.setDepartment("inventory");
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            invenNotification.setDatetime(new Date(System.currentTimeMillis()));
            return invenNotification;
        }


        public void setOrderId(Integer orderId){
            this.orderId=orderId;
        }
        public void setItemId(Integer itemId){
            this.itemId=itemId;
        }
        public void setQuantity(Integer quantity){
            this.quantity=quantity;
        }
        public Integer getOrderId(){
            return this.orderId;
        }
        public Integer getItemId(){
            return this.itemId;
        }
        public Integer getQuantity(){
            return this.quantity;
        }
        public String getDepartment() {
            return department;
        }
        public void setDepartment(String department) {
            this.department = department;
        }
        public Date getDatetime() {
            return datetime;
        }
    
        public void setDatetime(Date datetime) {
            this.datetime = datetime;
        }
    }
    
    
}
