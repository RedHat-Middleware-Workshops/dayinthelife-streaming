package module3;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.component.infinispan.InfinispanOperation;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;

//oc delete configmap shippingconsole-config  
//oc apply -f shippingconsole-config.yaml
// 
//kamel run -d camel-infinispan -d camel-bean -d camel-swagger-java -d camel-jackson -d camel-undertow  -d mvn:org.wildfly.security:wildfly-elytron:1.11.2.Final --configmap shippingconsole-config ConsoleService.java --dev
//--trait debug.enabled=true --property logging.level.org.apache.camel=DEBUG

public class ConsoleService extends RouteBuilder{


  
    @Override
    public void configure() throws Exception{

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
            .post("/premium")
                .to("direct:premium")
            .post("/standard")
                .to("direct:standard");

        from("direct:premium")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
            .setHeader(InfinispanConstants.KEY).constant("premium")
            .to("infinispan:default")         
        ;
    
        from("direct:standard")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
            .setHeader(InfinispanConstants.KEY).constant("standard")
            .to("infinispan:default")         
        ;

        
    }



       



        
}