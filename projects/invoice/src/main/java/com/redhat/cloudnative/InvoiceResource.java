package com.redhat.cloudnative;

import java.text.DecimalFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.vertx.core.json.JsonObject;

@Path("/")
public class InvoiceResource {

    @POST
    @Path("/notify/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String notify(Order order) throws Exception {

        Thread.sleep(30000);

        return processOrder(order);
    }

    protected String processOrder(Order order) {
        JsonObject output = new JsonObject();
        output.put("orderId", order.getOrderId());
        output.put("itemId", order.getItemId());
        output.put("department", "invoicing");
        output.put("datetime", System.currentTimeMillis());
        output.put("amount", order.getQuantity() * order.getPrice());
        output.put("currency", "USD");
        DecimalFormat numformat = new DecimalFormat("#");
        output.put("invoiceId", numformat.format(Math.floor(100000 + Math.random() * 999999)) );
        return output.encode();
    }
}