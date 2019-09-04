/* 
kamel run --name=inventory-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 InventoryService.java
kamel run --name=inventory-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d camel-ahc-ws -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 InventoryService.java --dev
kamel run --name=inventory-service InventoryService.java
*/
import java.util.HashMap;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jackson.JacksonDataFormat;
//import org.apache.camel.component.websocket.WebsocketComponent;

public class InventoryService extends RouteBuilder {

   // String BROKER_URL = "tcp://broker-amq-tcp.user1.svc:61616";
    
    
    @Override
    public void configure() throws Exception {
        
        restConfiguration()
            .component("undertow")
            .apiContextPath("/api-doc")
            .apiProperty("cors", "true")
            .apiProperty("api.title", "Flight Center")
            .apiProperty("api.version", "1.0")
            .port("8080")
            .bindingMode(RestBindingMode.json);

        rest()
            .post("/notify/order")
                .to("direct:notify");

        getContext().addComponent("activemq", ActiveMQComponent.activeMQComponent(BROKER_URL));
        
        /** WebsocketComponent websocket = new WebsocketComponent();
        websocket.setHost("dilii-ui.pushed.svc");
        websocket.setMinThreads(4);
        websocket.setMaxThreads(5);
        getContext().addComponent("websocket", websocket);**/
        
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Order.class);
        

        from("direct:notify")
            .marshal(jacksonDataFormat)
            .log("Inventory Notified ${body}")
            .to("ahc-ws://dilii-ui:8181/echo?sendToAll=true")
            ;
/*
        from("activemq:topic:incomingorders?username=amq&password=password")
            .marshal(jacksonDataFormat)
            .log("Inventory Notified ${body}")
            ;*/
    }


    
    private static class Order implements java.io.Serializable{
        private static final long serialVersionUID = 1L;
        
        private Integer orderId;
        private Integer itemId;
        private String orderItemName;
        private Integer quantity;
        private Integer price;
        private String address;
        private Integer zipCode;

        

        public void setOrderId(Integer orderId){
            this.orderId=orderId;
        }
        public void setItemId(Integer itemId){
            this.itemId=itemId;
        }
        public void setOrderItemName(String orderItemName){
            this.orderItemName=orderItemName;
        }
        public void setQuantity(Integer quantity){
            this.quantity=quantity;
        }
        public void setPrice(Integer price){
            this.price=price;
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
        public String getOrderItemName(){
            return this.orderItemName;
        }
        public Integer getQuantity(){
            return this.quantity;
        }
        public Integer getPrice(){
            return this.price;
        }
        public String getAddress(){
            return this.address;
        }
        public Integer getZipCode(){
            return this.zipCode;
        }
    }
    
}
