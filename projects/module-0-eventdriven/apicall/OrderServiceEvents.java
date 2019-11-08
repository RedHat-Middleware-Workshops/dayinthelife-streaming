/** 
kamel run --name=order-service-events -d camel-amqp -d camel-swagger-java -d camel-jackson -d camel-undertow  OrderServiceEvents.java --dev
*/
import java.util.HashMap;
import javax.ws.rs.core.MediaType;
import org.apache.camel.PropertyInject;
import org.apache.camel.BindToRegistry;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;

public class OrderServiceEvents extends RouteBuilder {

    @BindToRegistry
    public javax.jms.ConnectionFactory connectionFactory() {
        return new org.apache.qpid.jms.JmsConnectionFactory("amqp://event-bus-amqp-0-svc.messaging.svc.cluster.local");
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("undertow")
            .apiContextPath("/api-doc")
            .apiProperty("cors", "true")
            .apiProperty("api.title", "Order API")
            .apiProperty("api.version", "1.0")
            .enableCORS(true)
            .port("8080")
            .bindingMode(RestBindingMode.json);

        rest()
            .post("/place")
                .to("direct:placeorder");

        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Order.class);

        from("direct:placeorder")
            .unmarshal().json()
            .log("placeorder--> ${body}")
            .marshal().json()
            .to("amqp:topic:notify/orders?exchangePattern=InOnly")
            .setHeader(Exchange.HTTP_RESPONSE_CODE).constant(202)
            .setBody(simple("Order Placed"))
            .to("log:info");
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
        private String datetime;
        private String department;

        public void setDepartment(String department){
            this.department = department;
        }
        public String getDepartment(){
            return "Inventory";
        }
        public void setDatetime(String datetime){
            this.datetime = datetime;
        }
        public String getDatetime(){
            return "2019-08-08 22:19:99";
        }
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
