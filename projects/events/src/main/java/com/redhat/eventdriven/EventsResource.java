package com.redhat.eventdriven;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class EventsResource {

    Logger log = LoggerFactory.getLogger(EventsResource.class);

    /* TODO add notifications Channel */


    /* TODO add consume Path */

}