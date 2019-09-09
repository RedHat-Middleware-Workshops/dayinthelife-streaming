/** 
kamel run --name=shipping-service -d camel-jackson -d camel-ahc-ws -d camel-amqp  ShippingService.java
*/
import java.util.HashMap;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.amqp.AMQPConnectionDetails;
import org.apache.camel.component.jackson.JacksonDataFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ShippingService extends RouteBuilder {

    String BROKER_URL = "amqp://messaging-7rm3r5j1m3.workshop-operators.svc"+":5672"+"?amqp.saslMechanisms=PLAIN";
    String USERNAME = "user";
    String PWD = "enmasse";
    
    @Override
    public void configure() throws Exception {
        

        AMQPConnectionDetails amqpDetail = new AMQPConnectionDetails(BROKER_URL,USERNAME,PWD,false);
        getContext().getRegistry().bind("amqpDetail",AMQPConnectionDetails.class,amqpDetail);
        
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Map.class);
        JacksonDataFormat shippingDataFormat = new JacksonDataFormat();
        shippingDataFormat.setUnmarshalType(ShippingNotification.class);
        
        
        from("amqp:topic:incomingorders?subscriptionDurable=false")
            .unmarshal(jacksonDataFormat)
            .bean(ShippingNotification.class, "getShippingNotification(${body['orderId']},${body['itemId']},${body['quantity']},${body['address']},${body['zipCode']} )")
            .marshal(shippingDataFormat)
            .convertBodyTo(String.class)
            .log("Shipping Local Notified ${body}")
            .to("ahc-ws://dilii-uiws:8181/echo")
            ;
    }

    private static class ShippingNotification {
        private Integer orderId;
        private Integer itemId;
        private Integer quantity;
        private String address;
        private Integer zipCode;
        private String department;
        private Date datetime;

        public static ShippingNotification getShippingNotification(Integer orderId, Integer itemId, Integer quantity, String address, Integer zipCode ){
            ShippingNotification shippingNotification  = new ShippingNotification();
            shippingNotification.setOrderId(orderId);
            shippingNotification.setItemId(itemId);
            shippingNotification.setQuantity(quantity);
            shippingNotification.setAddress(address);
            shippingNotification.setZipCode(zipCode);
            shippingNotification.setDepartment("shippinglocal");
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            shippingNotification.setDatetime(new Date(System.currentTimeMillis()));
            return shippingNotification;
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
        public void setAddress(String address){
            this.address=address;
        }
        public void setZipCode(Integer zipCode){
            this.zipCode=zipCode;
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
        public String getAddress(){
            return this.address;
        }
        public Integer getZipCode(){
            return this.zipCode;
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