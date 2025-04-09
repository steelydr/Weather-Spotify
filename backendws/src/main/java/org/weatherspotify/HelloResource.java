package org.weatherspotify;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

    @Inject
    Template hello; // Inject the Qute template

    // Handle GET request to display "Hello"
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance displayHello() {
        // Render the template with a simple "Hello" message
        return hello.data("message", "Welcome to Qute with Quarkus!");
    }
}
