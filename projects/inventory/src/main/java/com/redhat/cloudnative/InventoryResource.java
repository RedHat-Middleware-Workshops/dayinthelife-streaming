package com.redhat.cloudnative;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class InventoryResource {

    @POST
    @Path("/notify/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InventoryNotification notify(Order order) {
        return InventoryNotification.getInventoryNotification(order);
    }
}