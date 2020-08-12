// camel-k: dependency=camel-infinispan dependency=camel-bean dependency=camel-jackson dependency=mvn:org.wildfly.security:wildfly-elytron:1.11.2.Final dependency=mvn:io.netty:netty-codec:4.1.49.Final configmap=standardshipping-config

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

//oc delete configmap standardshipping-config  
//oc apply -f standardshipping-config.yaml
// 
//kamel run -d camel-infinispan -d camel-bean -d camel-jackson -d mvn:org.wildfly.security:wildfly-elytron:1.11.2.Final --configmap standardshipping-config StandardShipping.java --dev
//--trait debug.enabled=true --property logging.level.org.apache.camel=DEBUG

public class StandardShipping extends RouteBuilder{

    static final int MAX_PER_CARGO=100;

  
    @Override
    public void configure() throws Exception{

        from("timer:cleanup?repeatCount=1")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.CLEAR)
            .setHeader(InfinispanConstants.KEY).constant("standard")
            .to("infinispan:default")         
        ;

        //USE API
        from("kafka:user1-standard?groupId=standard-shipping")
            .setHeader("marshmallow").simple("${body}")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
            .setHeader(InfinispanConstants.KEY).constant("standard")
            .to("infinispan:default")         
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.PUT)
            .setHeader(InfinispanConstants.KEY).constant("standard")
            .setHeader(InfinispanConstants.VALUE).method(this, "assignShipment(${body})")
            .log("${body}")
            .to("infinispan:default")
            
        ;
    }



    public Map assignShipment(Map<Integer, Integer> standardShipment){
            
        if(standardShipment == null)
            standardShipment = new HashMap<Integer, Integer>();

        standardShipment = addCargo(standardShipment);
        return standardShipment;

    }

    private Map<Integer, Integer> addCargo(Map<Integer, Integer> standardShipment){
            int lastCargoNo = 100;
            for(Map.Entry<Integer, Integer> item:standardShipment.entrySet()){
                lastCargoNo = item.getKey();
                if(item.getValue() < MAX_PER_CARGO){
                    item.setValue(item.getValue()+1);
                    return standardShipment;
                }
            }

            standardShipment.put(lastCargoNo+1, 1);
            return standardShipment;
        }


}