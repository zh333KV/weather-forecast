package bsuir.ict.weatherforecast.model;

public record WeatherData(
        String city,
        String source,
        double temperatureCelsius,
        int humidityPercent,
        String description,
        double wind
) {}
