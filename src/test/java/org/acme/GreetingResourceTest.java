package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testInstanceCount() {
        for (int i=1;i<20;i++) {
            expectEndpointMatch("/currentBeanCount", "count: 1");
            expectEndpointMatch("/producerCallsCount", i);
            expectEndpointMatch("/instanceDestroyedCount", i);
        }
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