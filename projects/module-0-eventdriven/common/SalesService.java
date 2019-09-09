/** 
kamel run --name=sales-service -d camel-swagger-java -d camel-jackson -d camel-undertow -d camel-ahc-ws -d camel-amqp SalesService.java
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

public class SalesService extends RouteBuilder {

    String BROKER_URL = "amqp://messaging-7rm3r5j1m3.workshop-operators.svc"+":5672"+"?amqp.saslMechanisms=PLAIN";
    String USERNAME = "user";
    String PWD = "enmasse";
    
    
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

        AMQPConnectionDetails amqpDetail = new AMQPConnectionDetails(BROKER_URL,USERNAME,PWD,false);
        getContext().getRegistry().bind("amqpDetail",AMQPConnectionDetails.class,amqpDetail);
                
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setUnmarshalType(Map.class);
        JacksonDataFormat salesDataFormat = new JacksonDataFormat();
        salesDataFormat.setUnmarshalType(SalesNotification.class);

        from("direct:notify")
            .marshal(jacksonDataFormat)
            .log("Sales Notified ${body}")
            .to("ahc-ws://dilii-uiws:8181/echo")
            ;

        from("amqp:topic:incomingorders?subscriptionDurable=false")
            .unmarshal(jacksonDataFormat)
            .bean(SalesNotification.class, "getSalesNotification(${body['orderId']},${body['price']} )")
            .marshal(salesDataFormat)
            .convertBodyTo(String.class)
            .log("Sales Notified ${body}")
            .to("ahc-ws://dilii-uiws:8181/echo")
            ;
        
    }


    
    private static class SalesNotification {
        private Integer orderId;
        private Integer price;
        private String department;
        private Date datetime;

        public static SalesNotification getSalesNotification(Integer orderId, Integer price ){
            SalesNotification salesNotification  = new SalesNotification();
            salesNotification.setOrderId(orderId);
            salesNotification.setPrice(price);
            salesNotification.setDepartment("sales");
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            salesNotification.setDatetime(new Date(System.currentTimeMillis()));
            return salesNotification;
        }

        public void setOrderId(Integer orderId){
            this.orderId=orderId;
        }
        public Integer getOrderId(){
            return this.orderId;
        }
        public void setPrice(Integer price){
            this.price=price;
        }
        public Integer getPrice(){
            return this.price;
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