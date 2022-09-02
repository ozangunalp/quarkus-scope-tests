package org.acme;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    @Disabled
    public void testInstanceCount() {
        for (int i=1;i<20;i++) {
            expectEndpointMatch("/currentBeanCount", "count: 1");
            expectEndpointMatch("/producerCallsCount", i);
            expectEndpointMatch("/instanceDestroyedCount", i);
        }
    }

    @Disabled
    void testCustomContextCount() {
        expectEndpointMatch("/actOnGlobalThingCustomContext", "count: 1");
    }

    @Disabled
    void testCustomContextCountActivateRequestContext() {
        expectEndpointMatch("/actOnGlobalThingCustomContextActivateRequestContext", "count: 1");
    }

    @Disabled
    void testVertxContextCount() {
        expectEndpointMatch("/actOnGlobalThingVertxContext", "count: 1");
    }

    @Disabled
    void testCustomContextCountMutiny() {
        expectEndpointMatch("/actOnGlobalThingCustomContextMutiny", "count: 1");
    }

    @Disabled
    void testVertxContextCountMutiny() {
        expectEndpointMatch("/actOnGlobalThingVertxContextMutiny", "count: 1");
    }

    private void expectEndpointMatch(String endpoint, int expected) {
        expectEndpointMatch( endpoint, String.valueOf( expected ) );
    }

    private void expectEndpointMatch(String endpoint, String expected) {
        given()
                .when().get( endpoint )
                .then()
                .statusCode( 200 )
                .body( is( expected ) );
    }

}