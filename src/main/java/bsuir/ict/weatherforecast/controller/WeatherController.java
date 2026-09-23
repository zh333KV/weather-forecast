package bsuir.ict.weatherforecast.controller;
import bsuir.ict.weatherforecast.model.WeatherData;
import bsuir.ict.weatherforecast.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @GetMapping("/")
    public String index(
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "source", required = false) String source,
            Model model
    ) {
        model.addAttribute("sources", weatherService.availableSources());
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedSource", source);
        if (city != null && !city.isBlank() && source != null && !source.isBlank()) {
            try {
                WeatherData weatherData = weatherService.getWeather(city.trim(), source);
                model.addAttribute("weather", weatherData);
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        return "index";
    }
}
