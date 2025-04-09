package org.weatherspotify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/track")
public class PhoneTrackingResource {

    // Inject the Qute template
    @Inject
    Template phoneNumber;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance trackPhone(@QueryParam("number") String phoneNumberParam) {
        // Simulate fetching location for the phone number
        String location = getGeolocationForIP(phoneNumberParam);

        // Pass the phone number and location data to the Qute template
        return phoneNumber.data("number", phoneNumberParam).data("location", location);
    }

    // Example method to fetch geolocation data from an IP-based service (replace with real phone tracking service if available)
    private String getGeolocationForIP(String phoneNumber) {
        String apiKey = "4ea9067b9f292a"; // Replace with your API key
        String apiUrl = "https://ipinfo.io/" + phoneNumber + "/?token=" + apiKey;
        StringBuilder inline = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return "Unable to fetch location";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                inline.append(line);
            }
            reader.close();

            // Manually parse the JSON response without using JSONObject
            String json = inline.toString();
            return parseLocationFromJson(json);

        } catch (Exception e) {
            return "Error fetching location";
        }
    }

    // Manually parse JSON to extract location fields
    private String parseLocationFromJson(String json) {
        String city = extractField(json, "city");
        String region = extractField(json, "region");
        String country = extractField(json, "country");

        if (city != null && region != null && country != null) {
            return city + ", " + region + ", " + country;
        } else {
            return "Location data not available";
        }
    }

    // Helper method to extract field values from the JSON string
    private String extractField(String json, String fieldName) {
        String searchKey = "\"" + fieldName + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null;
        }
        return json.substring(startIndex, endIndex);
    }
}
