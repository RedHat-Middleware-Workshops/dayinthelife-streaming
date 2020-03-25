package com.redhat.eventdriven;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Channel;

@Path("/")
public class EventsResource {

    Logger log = LoggerFactory.getLogger(EventsResource.class);

    /* TODO add notifications Channel */

    /* TODO add consume Path */

}