package com.redhat.workshop.dil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.event.Observes;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Monitor {

    private static final Logger LOGGER = Logger.getLogger("Monitor");

    @Inject
    KafkaStreams streams;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        try {
        
            //Thread.sleep(3000);
        
            streams.start();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
    
   
    

}