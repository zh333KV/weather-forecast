package bsuir.ict.weatherforecast.service;
import bsuir.ict.weatherforecast.model.WeatherData;
import bsuir.ict.weatherforecast.provider.WeatherProvider;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final Map<String, WeatherProvider> providerByKey;
    public WeatherService(List<WeatherProvider> providers) { // Spring инжектит все бины, которые реализуют WeatherProvider
        this.providerByKey = providers.stream()
                .collect(Collectors.toMap(WeatherProvider::sourceKey, p -> p));
    }

    public List<SourceInfo> availableSources() {
        return providerByKey.values().stream()
                .map(p -> new SourceInfo(p.sourceKey(), p.sourceName()))
                .sorted(Comparator.comparing(SourceInfo::name))
                .toList();
    }

    public WeatherData getWeather(String city, String sourceKey) {
        WeatherProvider provider = providerByKey.get(sourceKey);
        if (provider == null) {
            throw new IllegalArgumentException("Неизвестный источник погоды: " + sourceKey);
        }
        return provider.fetchByCity(city);
    }

    public record SourceInfo(String key, String name) {
    }
}
