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

public class PremiumShipping extends RouteBuilder{

    static final int MAX_PER_CARGO=10;


    @Override
    public void configure() throws Exception{

    /*
     * Insert Camel routes here
     */
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
