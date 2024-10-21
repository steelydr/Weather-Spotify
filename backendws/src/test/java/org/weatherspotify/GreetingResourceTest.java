package org.weatherspotify;

import static org.hamcrest.CoreMatchers.containsString;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        RestAssured.given()
          .when().get("/")
          .then()
             .statusCode(200) // Check for HTTP 200 response
             .contentType("text/html") // Ensure the content type is HTML
             .body(containsString("<!DOCTYPE html>")) // Ensure the response contains HTML
             .body(containsString("Weather Information")); // Ensure it contains the expected message
    }
}
