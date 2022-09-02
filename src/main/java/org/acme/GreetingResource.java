package org.acme;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.EventLoopContext;
import io.vertx.core.impl.VertxInternal;
import io.vertx.mutiny.core.Vertx;

@Path("/")
public class GreetingResource {

    @Inject
    GloballyScopedThing globalThing;

    @Inject
    Vertx vertx;

    EventLoopContext context;

    @GET
    @Path("/actOnGlobalThingCustomContext")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> actOnGlobalThingCustomContext() {
        return Uni.createFrom().emitter(e -> {
            context.runOnContext(x -> {
                try {
                    e.complete(globalThing.actOnGlobalThing("item"));
                } catch (Throwable t) {
                    e.fail(t);
                }
            });
        });
    }

//    @GET
//    @Path("/actOnGlobalThingCustomContextActivateRequestContext")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String actOnGlobalThingCustomContextActivateRequestContext() {
//        return Uni.createFrom().<String>emitter(e -> {
//            context.runOnContext(x -> {
//                try {
//                    e.complete(actWithRequest("item"));
//                } catch (Throwable t) {
//                    e.fail(t);
//                }
//            });
//        }).await().indefinitely();
//    }

    @GET
    @Path("/actOnGlobalThingCustomContextActivateRequestContext")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> actOnGlobalThingCustomContextActivateRequestContext() {
        return Uni.createFrom().emitter(e -> {
            context.runOnContext(x -> {
                context.runOnContext(y -> {
                try {
                    e.complete(actWithRequest("item"));
                } catch (Throwable t) {
                    e.fail(t);
                }
                });
            });
        });
    }

    @ActivateRequestContext
    String actWithRequest(String item) {
        return globalThing.actOnGlobalThing(item);
    }

    @GET
    @Path("/actOnGlobalThingVertxContext")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> actOnGlobalThingVertxContext() {
        return Uni.createFrom().emitter(e -> {
            vertx.runOnContext(() -> {
                try {
                    e.complete(globalThing.actOnGlobalThing("item"));
                } catch (Throwable t) {
                    e.fail(t);
                }
            });
        });
    }

    @GET
    @Path("/actOnGlobalThingCustomContextMutiny")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> actOnGlobalThingCustomContextMutiny() {
        return Uni.createFrom().item("item")
                .emitOn(r -> context.runOnContext(x -> r.run()))
                .map(globalThing::actOnGlobalThing);
    }

    @GET
    @Path("/actOnGlobalThingVertxContextMutiny")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> actOnGlobalThingVertxContextMutiny() {
        return Uni.createFrom().item("item")
                .emitOn(vertx::runOnContext)
                .map(globalThing::actOnGlobalThing);
    }

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
        return CriticalResourceManager.disposerCallsCount();
    }

    @GET
    @Path("/instanceDelta")
    @Produces(MediaType.TEXT_PLAIN)
    public int instanceDelta() {
        return CriticalResourceManager.producerDisposerDelta();
    }
}