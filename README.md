# 🌤️ Weather Forecast Service (Spring Boot & Docker)

An educational project for displaying current weather data from different weather providers, including **OpenWeatherMap** and **Open-Meteo**.

## 🚀 How to Run the Project Locally

You can run the application in two ways: directly with Java or inside an isolated Docker container.

### IMPORTANT: Getting an API Key

To use the application with **OpenWeatherMap**, you need a free API key:

1. Register on [OpenWeatherMap](https://openweathermap.org).
2. Copy `YOUR_API_KEY` from your account settings.

---

### Option 1: Run with Docker (Recommended)

Make sure **Docker Desktop** is installed and running.

1. **Build the Docker image:**

   ```bash
   docker build -t weather-app .
   ```

2. **Run the container:**

   Replace `YOUR_API_KEY` with your actual API key:

   ```bash
   docker run -d -p 8080:8080 -e WEATHER_API_KEY="YOUR_API_KEY" --name weather-container weather-app
   ```

3. **Check the application:**

   The application will be available at:

   http://localhost:8080

---

### Option 2: Run Without Docker (Locally)

**Java 17** is required.

1. Set the environment variable with your API key in the terminal:

   **Windows (PowerShell):**

   ```powershell
   $env:WEATHER_API_KEY="YOUR_API_KEY"
   ```

   **Linux / macOS:**

   ```bash
   export WEATHER_API_KEY="YOUR_API_KEY"
   ```

2. Build and run the project using the Maven Wrapper:

   ```bash
   ./mvnw spring-boot:run
   ```
