package bsuir.ict.weatherforecast.provider;

import bsuir.ict.weatherforecast.model.WeatherData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.List;

@Component
public class OpenMeteoWeatherProvider implements WeatherProvider {
    private final RestClient restClient = RestClient.create();
    @Override
    public String sourceKey() {
        return "open-meteo";
    }
    @Override
    public String sourceName() {
        return "Open Meteo";
    }

    @Override
    public WeatherData fetchByCity(String city) {
        GeoResponse geoResponse = restClient.get()
                .uri("https://geocoding-api.open-meteo.com/v1/search?name={city}&count=1&language=ru&format=json", city)
                .retrieve()
                .body(GeoResponse.class);

        if (geoResponse == null || geoResponse.results() == null || geoResponse.results().isEmpty()) {
            throw new RestClientException("Город не найден в Open Meteo");
        }

        GeoResult location = geoResponse.results().get(0);
        ForecastResponse forecastResponse = restClient.get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m",
                        location.latitude(), location.longitude())
                .retrieve()
                .body(ForecastResponse.class);

        if (forecastResponse == null || forecastResponse.current() == null) {
            throw new RestClientException("Не удалось получить текущую погоду от Open Meteo");
        }

        return new WeatherData(
                location.name(),
                sourceName(),
                forecastResponse.current().temperature(),
                forecastResponse.current().humidity(),
                weatherCodeToText(forecastResponse.current().weatherCode()),
                forecastResponse.current().speed()
        );
    }

    private String weatherCodeToText(int code) {
        return switch (code) {
            case 0 -> "Ясно";
            case 1, 2, 3 -> "Переменная облачность";
            case 45, 48 -> "Туман";
            case 51, 53, 55, 56, 57 -> "Морось";
            case 61, 63, 65, 66, 67, 80, 81, 82 -> "Дождь";
            case 71, 73, 75, 77, 85, 86 -> "Снег";
            case 95, 96, 99 -> "Гроза";
            default -> "Неизвестные условия";
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GeoResponse(List<GeoResult> results) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GeoResult(String name, double latitude, double longitude) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ForecastResponse(Current current) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Current(
            @JsonProperty("temperature_2m") double temperature,
            @JsonProperty("relative_humidity_2m") int humidity,
            @JsonProperty("weather_code") int weatherCode,
            @JsonProperty("wind_speed_10m") double speed
    ) {}
}
