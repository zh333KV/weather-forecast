package bsuir.ict.weatherforecast.provider;
import bsuir.ict.weatherforecast.model.WeatherData;

public interface WeatherProvider {
    String sourceKey();
    String sourceName();
    WeatherData fetchByCity(String city);
}
