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

//oc delete configmap premiumshipping-config  
//oc apply -f premiumshipping-config.yaml
// 
//kamel run -d camel-infinispan -d camel-bean -d camel-jackson -d mvn:org.wildfly.security:wildfly-elytron:1.11.2.Final --configmap premiumshipping-config PremiumShipping.java --dev
//--trait debug.enabled=true --property logging.level.org.apache.camel=DEBUG

public class PremiumShipping extends RouteBuilder{

    static final int MAX_PER_CARGO=10;

  
    @Override
    public void configure() throws Exception{

        from("timer:cleanup?repeatCount=1")
        .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.CLEAR)
        .setHeader(InfinispanConstants.KEY).constant("premium")
        .to("infinispan:default")         
        ;
        

        from("kafka:user1-premium?groupId=premium-shipping")
        .streamCaching()
            .unmarshal(new JacksonDataFormat(Map.class))
            .log("Input --> ${body}")
            .setHeader("marshmallow").simple("${body}")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
            .setHeader(InfinispanConstants.KEY).constant("premium")
            .to("infinispan:default")         
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.PUT)
            .setHeader(InfinispanConstants.KEY).constant("premium")
            .setHeader(InfinispanConstants.VALUE).method(this, "assignShipment(${body}, ${header.marshmallow})")
            .log("${body}")
            .to("infinispan:default")
            
        ;
    }



        public Map assignShipment(Map<Integer, List<Map>> premiumShipment, Map marshmallow){
            
            if(premiumShipment == null)
                premiumShipment = new HashMap<Integer, List<Map>>();

            premiumShipment = addCargo(premiumShipment,marshmallow);
                
            return premiumShipment;

        }

        private Map<Integer, List<Map>> addCargo(Map<Integer, List<Map>> premiumShipment, Map marshmallow){
            int lastCargoNo = 100;
            for(Map.Entry<Integer, List<Map>> item:premiumShipment.entrySet()){
                lastCargoNo = item.getKey();
                if(item.getValue().size() < MAX_PER_CARGO){
                    item.getValue().add(marshmallow);
                    return premiumShipment;
                }
            }

            List newcargo = new ArrayList<Map>();
            newcargo.add(marshmallow);
            premiumShipment.put(lastCargoNo+1, newcargo);
            return premiumShipment;
        }



        
}