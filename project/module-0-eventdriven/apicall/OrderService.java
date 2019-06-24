/** 
kamel run --name=order-service-api -d camel-swagger-java -d camel-jackson -d camel-undertow  OrderService.java --dev
*/
import java.util.HashMap;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class OrderService extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        restConfiguration()
            .component("undertow")
            .apiContextPath("/api-doc")
            .apiProperty("cors", "true")
            .apiProperty("api.title", "Order API")
            .apiProperty("api.version", "1.0")
            .port("8080")
            .bindingMode(RestBindingMode.json);

        rest()
            .post("/place")
                .to("direct:placeorder");

        //getContext().addComponent("activemq", ActiveMQComponent.activeMQComponent(BROKER_URL));
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Order.class);
        

        from("direct:placeorder")
            .log("placeorder--> ${body}")
            .marshal(jacksonDataFormat)
            .log("placeorder 2 --> ${body}")
           // .removeHeader("*")
           // .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
            //.toD("http4://inventory-service/notify/order?bridgeEndpoint=true")
            ;

        
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