package org.acme;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testInstanceCount() {
        for (int i = 1; i < 20; i++) {
            expectEndpointMatch("/currentBeanCount", "count: 1");
            expectEndpointMatch("/instanceDelta", 0);
        }
    }

    @Test
    void testCustomContextCount() {
        expectEndpointErrorContains("/actOnGlobalThingCustomContext", 500, "javax.enterprise.context.ContextNotActiveException");
        expectEndpointMatch("/instanceDelta", 0);
    }

    @Test
    void testCustomContextCountActivateRequestContext() {
        expectEndpointMatch("/actOnGlobalThingCustomContextActivateRequestContext", "count: 1");
        expectEndpointMatch("/instanceDelta", 0);
    }

    @Test
    void testVertxContextCount() {
        expectEndpointMatch("/actOnGlobalThingVertxContext", "count: 1");
        expectEndpointMatch("/instanceDelta", 0);
    }

    @Test
    void testCustomContextCountMutiny() {
        expectEndpointMatch("/actOnGlobalThingCustomContextMutiny", "count: 1");
        expectEndpointMatch("/instanceDelta", 0);
    }

    @Test
    void testVertxContextCountMutiny() {
        expectEndpointMatch("/actOnGlobalThingVertxContextMutiny", "count: 1");
        expectEndpointMatch("/instanceDelta", 0);
    }

    private void expectEndpointMatch(String endpoint, int expected) {
        expectEndpointMatch(endpoint, String.valueOf(expected));
    }

    private void expectEndpointMatch(String endpoint, String expected) {
        given()
                .when().get(endpoint)
                .then()
                .statusCode(200)
                .body(is(expected));
    }

    private void expectEndpointErrorContains(String endpoint, int status, String expected) {
        given()
                .when().get(endpoint)
                .then()
                .statusCode(status)
                .body(containsString(expected));
    }

}