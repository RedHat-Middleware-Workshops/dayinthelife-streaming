// camel-k: dependency=camel-infinispan dependency=camel-bean dependency=camel-jackson dependency=camel-swagger-java configmap=costadvice-config

package module3;
import org.apache.camel.builder.RouteBuilder;
import java.util.Map;
import java.util.HashMap;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.Body;

//oc delete cm costadvice-config
//oc apply -f costadvice-config.yaml
//
//kamel run -d camel-bean -d camel-jackson --configmap costadvice-config CostAdvice.java --dev
//--trait debug.enabled=true --property logging.level.org.apache.camel=DEBUG

public class CostAdvice extends RouteBuilder{

    static final int MAX_PER_CARGO=10;
    /** Not Possible in Current Supported Java Version
    static final Map COST_FACTOR = Map.ofEntries(
        entry(101, 8.0),
        entry(302, 5.4),
        entry(787, 7.9),
        entry(645, 8.7),
        entry(555, 8.7),
        entry(460, 7.6),
        entry(892, 7.5)
    );*/

    static Map<Integer, Double> COST_FACTOR = new HashMap<Integer, Double>();
    static{
        COST_FACTOR.put(101, 8.0);
        COST_FACTOR.put(302, 5.4);
        COST_FACTOR.put(787, 7.9);
        COST_FACTOR.put(645, 8.7);
        COST_FACTOR.put(555, 8.7);
        COST_FACTOR.put(460, 7.6);
        COST_FACTOR.put(892, 7.5);
    }

    Map<Integer, Double> farmCost = new HashMap<Integer, Double>();

    @Override
    public void configure() throws Exception{

        from("kafka:costcenter?groupId=costsender")
            .unmarshal(new JacksonDataFormat(Map.class))
            .log("Input --> ${body}")
            .bean(this, "calculate")
        ;

    }


    public void calculate(@Body Map<Integer, Integer> input){
        int farmid = input.get("farmid");
        double batchcost = input.get("batchcnt") * COST_FACTOR.get(farmid);

        if(farmCost.get(farmid) != null && farmCost.get(farmid) >0)
            farmCost.put(farmid, farmCost.get(farmid)+batchcost);
        else
            farmCost.put(farmid, batchcost);
    }



}
