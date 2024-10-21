package org.weatherspotify;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/send-location")
public class LocationResource {

    @Inject
    Template location; // Inject the Qute template

    // Handle GET request (for testing)
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getLocationTest() {
        // Test with some default values
        return location.data("latitude", 0.0, "longitude", 0.0);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance receiveLocation(JsonObject locationData) {
        double latitude = locationData.getJsonNumber("latitude").doubleValue();
        double longitude = locationData.getJsonNumber("longitude").doubleValue();

        // Pass the location data to the template
        return location.data("latitude", latitude, "longitude", longitude);
    }
}
