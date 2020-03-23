
package module3.simulator;

import org.apache.camel.builder.RouteBuilder;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


//kamel run --name edge-simulator EdgeSimulator.java  -d camel-jackson -d camel-bean  --configmap edge-config --dev
//oc create configmap edge-config  --from-file=edge.properties
public class EdgeSimulator extends RouteBuilder{

  @Override
  public void configure() throws Exception{

  /*
   * Add your Camel Route Here
   */

  }

    public Map genRandomIoTData(){
        Random generator = new Random();
        Map iotData = new HashMap<String,Object>();

        Integer[] farms = {101, 302, 787, 645, 555, 460, 892};
        int randomIndex = generator.nextInt(farms.length);
        int batchcnt =  generator.nextInt(87) + 1;
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
