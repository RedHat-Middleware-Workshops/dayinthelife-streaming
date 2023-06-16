package com.redhat.eventdriven;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Multi;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/")
public class EventsResource {

    Logger log = LoggerFactory.getLogger(EventsResource.class);

    /* TODO add notifications Channel */


    /* TODO add consume Path */

}