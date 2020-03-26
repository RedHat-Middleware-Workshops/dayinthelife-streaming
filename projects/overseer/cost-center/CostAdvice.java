package module4;
import org.apache.camel.builder.RouteBuilder;
import java.util.Map;
import java.util.HashMap;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.Body;
import org.apache.camel.model.rest.RestBindingMode;



public class CostAdvice extends RouteBuilder{


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
            .get("/costadvice")
                .to("direct:costadvice")
        ;

        from("direct:costadvice")
            .bean(this, "getCostAdvice")
            .log("--> ${body}")
        ;

        from("kafka:costcenter?groupId=costadvisor")
            .unmarshal(new JacksonDataFormat(Map.class))
            .bean(this, "calculate")
        ;

    }


    public Map<Integer, Double> getCostAdvice(){
        return farmCost;
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
