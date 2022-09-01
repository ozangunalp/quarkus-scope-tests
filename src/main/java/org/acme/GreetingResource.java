package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @Inject
    GloballyScopedThing globalThing;

    @GET
    @Path("/currentBeanCount")
    @Produces(MediaType.TEXT_PLAIN)
    public String currentBeanInstanceCount() {
        return globalThing.currentBeanInstanceCount();
    }

    @GET
    @Path("/producerCallsCount")
    @Produces(MediaType.TEXT_PLAIN)
    public int producerCallsCount() {
        return CriticalResourceManager.producerCallsCount();
    }

    @GET
    @Path("/instanceDestroyedCount")
    @Produces(MediaType.TEXT_PLAIN)
    public int instanceDestroyedCount() {
        return CriticalResourceManager.producerCallsCount();
    }
}