package com.redhat.workshop.dil;

import org.kie.kogito.rules.DataSource;
import org.kie.kogito.rules.DataStream;
import org.kie.kogito.rules.RuleUnitData;

public class DisasterUnit implements RuleUnitData {

    private  DataStream<HarvestinFive> eventStream;

    public DisasterUnit(DataStream<HarvestinFive> eventStream) {
        this.eventStream = eventStream;
    } 

    public DisasterUnit() {
        this(DataSource.createStream());
    }

    public DataStream<HarvestinFive> getEventStream() {
        return eventStream;
    }
}