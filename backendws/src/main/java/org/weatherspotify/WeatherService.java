package org.weatherspotify;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/current.json")
@RegisterRestClient(configKey = "weather-api")
public interface WeatherService {

    @GET
    WeatherResponse getWeather(@QueryParam("key") String apiKey, 
                               @QueryParam("q") String location, 
                               @QueryParam("aqi") String aqi);
}
