package io.syndesis.extension.uuid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import io.syndesis.extension.api.Step;
import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.DataShape;


@Action(
	id = "generateUUID", 
	name = "AssignUUID", 
	description = "Assign ID for your orders", 
	outputDataShape = @DataShape(
        kind= "java",
        type = "io.syndesis.extension.uuid.FleurDeLuneID",
        name= "FleurDeLuneID"
    ),
	tags = { "streaminguuid", "extension" }
)
public class AssignUUIDAction implements Step {

	
	@Override
    public Optional<ProcessorDefinition> configure(CamelContext context, ProcessorDefinition route, Map<String, Object> parameters) {
		ObjectHelper.notNull(route, "route");
        
        FleurDeLuneID generatedResult = new FleurDeLuneID();
        generatedResult.setGeneratedID(Math.round((Math.random() * 999) + 1)+"");
        generatedResult.setGeneratedDT(System.currentTimeMillis());
		route.setBody().constant(generatedResult);
		
		return Optional.empty();
	}
}