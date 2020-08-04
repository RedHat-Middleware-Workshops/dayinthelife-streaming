// camel-k: dependency=mvn:org.postgresql:postgresql:42.2.10 dependency=camel-jdbc dependency=mvn:org.apache.commons:commons-dbcp2:2.7.0 configmap=sender-config

package module3;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.camel.BindToRegistry;


//kamel run Inventory.java --dev
//oc create configmap sender-config  --from-file=kafka.properties
//EIP - Split
//EIP - CBR
//EIP - Wiretap



public class Inventory extends RouteBuilder{

    @BindToRegistry("dataSource")
    public BasicDataSource datasoure() {
        BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://postgresql:5432/sampledb");
            dataSource.setUsername("user");
            dataSource.setPassword("password");
        return dataSource;
    }

    @Override
    public void configure() throws Exception{

        /*
         * Paste Camel Routes here
         */
      
    }

}
