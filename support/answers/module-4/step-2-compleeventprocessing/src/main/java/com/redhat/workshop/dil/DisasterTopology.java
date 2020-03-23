package com.redhat.workshop.dil;
import com.redhat.workshop.util.JsonPOJODeserializer;
import com.redhat.workshop.util.JsonPOJOSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Deserializer;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Map;
import java.util.HashMap;


import javax.inject.Inject;

import org.kie.kogito.rules.RuleUnit;
import org.kie.kogito.rules.RuleUnitInstance;

import java.time.Duration;

@ApplicationScoped
public class DisasterTopology{ 

    private static final String HARVEST_EVENT_TOPIC = "costcenter";
    private static final String DISASTER_EVENT_TOPIC = "disaster";
    private static final int DISASTER_HARVEST_INTERVAL=10;

    @Inject
    RuleUnit<DisasterUnit> alertRuleUnit;


    @Produces
    public Topology monitor() {

        //Setup POJO Serializer for Harvest Event
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<HarvestEvent> harvestEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", HarvestEvent.class);
        harvestEventSerializer.configure(serdeProps, false);

        final Deserializer<HarvestEvent> harvestEventDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", HarvestEvent.class);
        harvestEventDeserializer.configure(serdeProps, false);

        final Serde<HarvestEvent> harvestEventSerde = Serdes.serdeFrom(harvestEventSerializer, harvestEventDeserializer);

        //Setup POJO Serializer for Harvest Event
        final Serializer<HarvestinFive> harvestinFiveSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", HarvestinFive.class);
        harvestinFiveSerializer.configure(serdeProps, false);

        final Deserializer<HarvestinFive> harvestinFiveDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", HarvestinFive.class);
        harvestinFiveDeserializer.configure(serdeProps, false);

        final Serde<HarvestinFive> harvestinFiveSerde = Serdes.serdeFrom(harvestinFiveSerializer, harvestinFiveDeserializer);

        //Setup Drools RuleUnit 
        DisasterUnit disasterUnit = new DisasterUnit();
        RuleUnitInstance<DisasterUnit> alertsvcInstance = alertRuleUnit.createInstance(disasterUnit);


        //Build Topology to get harvest Info
        StreamsBuilder builder = new StreamsBuilder();

        KStream<Windowed<Long>, Integer> windowedharvestcnt = builder.stream(
            HARVEST_EVENT_TOPIC, /* input topic */
            Consumed.with(
                Serdes.String(), /* key serde */
                harvestEventSerde   /* value serde */
            )
        )
        .peek((key, value) -> System.out.println("Before key=" + key + ", value=" + value))
        .map((key, value) -> KeyValue.pair(value.getBatchtime(), value.getBatchcnt()))
        .groupByKey(
            Grouped.with( 
                Serdes.Long(), /* key */
                Serdes.Integer() /* value */
            )     
        )
        .windowedBy(TimeWindows.of(Duration.ofSeconds(DISASTER_HARVEST_INTERVAL)))
        .aggregate(
            () -> 0, /* initializer */
            (aggKey, newValue, aggValue) -> aggValue + newValue,
            Materialized.with(Serdes.Long(), Serdes.Integer())
        )
        .toStream()
        .peek((key, value) -> System.out.println("After key=" + key + ", value=" + value))
        ;

        //Pass data into Rules
        windowedharvestcnt.map(
                (key, value) -> {
                    HarvestinFive hin5 = new HarvestinFive();
                    hin5.setTotalCnt(value);
                    disasterUnit.getEventStream().append(hin5);
                    alertsvcInstance.fire();
                    return new KeyValue<>(key.key(),hin5);
                }
        )
        .peek((key, value) -> System.out.println("Result key=" + key + ", value=" + value))
        .to(DISASTER_EVENT_TOPIC, Produced.with(Serdes.Long(), harvestinFiveSerde));
        
       
        
       
        //harvestEventStreams.foreach((key, value) -> System.out.println(key + " => " + value));
        return builder.build();

    }

}