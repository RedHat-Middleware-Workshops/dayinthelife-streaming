// camel-k: dependency=camel-bean dependency=camel-jackson configmap=edge-config

package module3.simulator;

import org.apache.camel.builder.RouteBuilder;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class EdgeSimulator extends RouteBuilder{

  public static final int MIN = 150;
  @Override
  public void configure() throws Exception{

  from("timer:tick?fixedRate=true&period=5000")
.choice()
    .when(simple("{{simulator.run}}"))
        .setBody(method(this, "genRandomIoTData()"))
        .marshal().json()
        .log("${body}")
        .to("amqp:topic:mytopic?subscriptionDurable=false&exchangePattern=InOnly")
    .otherwise()
        .log("Nothing send ")
;

  }

    public Map genRandomIoTData(){
        Random generator = new Random();
        Map iotData = new HashMap<String,Object>();

        Integer[] farms = {101, 302, 787, 645, 555, 460, 892};
        int randomIndex = generator.nextInt(farms.length);
        int batchcnt =  generator.nextInt(87) + MIN;
        long batchtime = System.currentTimeMillis();

        List<Map> harvest = new ArrayList<Map>();

        for (int i = 0; i < batchcnt; i++) {
            harvest.add(genSingleEvent((batchtime*10)+i));
        }

        iotData.put("farmid", farms[randomIndex]);
        iotData.put("batchcnt", batchcnt);
        iotData.put("harvest", harvest);
        iotData.put("batchtime", batchtime);

        return iotData;
    }

    private Map genSingleEvent(long eventid){

        Map harvestEvent = new HashMap<String,Integer>();
        Random generator = new Random();
        harvestEvent.put("diameter", (generator.nextInt(5) + 1));
        harvestEvent.put("weight", ((generator.nextInt(5) + 1.0) + (((int)generator.nextInt(8)+1)*0.1)));
        harvestEvent.put("mmid", eventid);

        return harvestEvent;
    }

}
