package bsuir.ict.weatherforecast.provider;

import bsuir.ict.weatherforecast.model.WeatherData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.List;

@Component
public class OpenWeatherMapProvider implements WeatherProvider{
    private final RestClient restClient = RestClient.create();
    @Value("${weather.api-key}")
    private String apiKey;
    @Override
    public String sourceKey(){
        return "open-weather-map";
    }
    @Override
    public String sourceName(){
        return "Open Weather Map";
    }
    @Override
    public WeatherData fetchByCity(String city) {
        ForecastResponse forecastResponse = restClient.get()
                .uri("https://api.openweathermap.org/data/2.5/weather?q={city}" +
                                "&units=metric&lang=ru&appid={api}",
                        city, apiKey)
                .retrieve()
                .body(ForecastResponse.class);

        if (forecastResponse == null || forecastResponse.weather() == null || forecastResponse.main() == null) {
            throw new RestClientException("Не удалось получить текущую погоду от Open Weather Map");
        }
        String description = forecastResponse.weather().get(0).description().substring(0, 1).toUpperCase() + forecastResponse.weather().get(0).description.substring(1);
        return new WeatherData(
                city,
                sourceName(),
                forecastResponse.main().temp(),
                forecastResponse.main().humidity(),
                description,
                forecastResponse.wind().speed()
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ForecastResponse(List<Weather> weather, Main main, Wind wind) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Weather( @JsonProperty("description") String description) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Main(@JsonProperty("temp") double temp, @JsonProperty("humidity") int humidity) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Wind(@JsonProperty("speed") double speed) {}
}
