package org.weatherspotify;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @Inject
    Template greeting;

    @Inject
    @RestClient
    WeatherService weatherService;

    private static final String API_KEY = "c7160f62e83b4c5887514525241110";

    @GET
    @Blocking
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance hello() {
        String location = " 17.4065000,78.4772000"; // Default to San Francisco
        WeatherResponse weatherResponse = weatherService.getWeather(API_KEY, location, "no");

        String message = "Current temperature: " + weatherResponse.current.temp_c + "°C, " +
                         weatherResponse.current.condition.text+location;

        return greeting.data("message", message);
    }
}