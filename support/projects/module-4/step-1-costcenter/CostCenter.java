package module3;
import org.apache.camel.builder.RouteBuilder;
import java.util.Map;
import java.util.HashMap;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.Body;
//oc delete cm costcenter-config
//oc apply -f costcenter.yaml
//kamel run -d camel-jackson --configmap costcenter-config CostCenter.java --dev
//FILTER EIP


public class CostCenter extends RouteBuilder{

    @Override
    public void configure() throws Exception{

        from("amqp:topic:mytopic?subscriptionDurable=false")
        .unmarshal(new JacksonDataFormat(Map.class))
        .filter().method(MaskDetail.class, "mask")
        .log("${body}")
        .marshal().json()
        .to("kafka:user1-costcenter?groupId=costsender")
        ;
    }

    static class MaskDetail {
        public static Map mask(@Body Map farmharvest) {
            farmharvest.remove("harvest");
            return farmharvest;
        }
    }
}

