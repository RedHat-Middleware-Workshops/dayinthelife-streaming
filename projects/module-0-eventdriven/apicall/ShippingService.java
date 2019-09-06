/** 
kamel run --name=shipping-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 ShippingService.java
kamel run --name=shipping-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d camel-ahc-ws -d mvn:org.apache.activemq:activemq-camel:5.15.9 -d mvn:org.apache.activemq:activemq-client:5.15.9 ShippingService.java --dev
kamel run --name=shipping-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d camel-ahc-ws ShippingService.java
*/
import java.util.HashMap;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.component.jackson.JacksonDataFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ShippingService extends RouteBuilder {

    
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

        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Map.class);
        JacksonDataFormat shippingDataFormat = new JacksonDataFormat();
        shippingDataFormat.setUnmarshalType(ShippingNotification.class);
        

        from("direct:notify")
            .log("Shipping Local Notified ${body}")
            .bean(ShippingNotification.class, "getShippingNotification(${body['orderId']},${body['itemId']},${body['quantity']},${body['address']},${body['zipCode']} )")
            .marshal(shippingDataFormat)
            .convertBodyTo(String.class)
            .to("ahc-ws://dilii-ui:8181/echo")
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