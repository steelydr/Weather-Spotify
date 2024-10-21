package org.weatherspotify;

public class WeatherResponse {
    public Current current;

    public static class Current {
        public double temp_c;
        public Condition condition;

        public static class Condition {
            public String text;
            public String icon;
        }
    }
}
