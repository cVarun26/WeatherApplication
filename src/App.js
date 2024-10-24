import { useEffect, useState } from "react";
import axios from "axios";
import WeatherChart from "./components/WeatherChart";
import "./App.css";

const cities = [
  "Delhi",
  "Mumbai",
  "Chennai",
  "Bangalore",
  "Kolkata",
  "Hyderabad",
];

const App = () => {
  const [weatherData, setWeatherData] = useState({});
  const [selectedCity, setSelectedCity] = useState("Delhi");
  const [threshold, setThreshold] = useState(""); // State to store threshold value
  const [alertResponse, setAlertResponse] = useState(""); // State to show response from /setAlert API

  useEffect(() => {
    const fetchWeatherData = async () => {
      try {
        const response = await axios.get("http://localhost:8090/weather");
        const data = response.data;

        const cityData = {};
        data.forEach((entry) => {
          cityData[entry.city] = entry;
        });

        setWeatherData(cityData);
      } catch (error) {
        console.error("Error fetching weather data:", error);
      }
    };

    fetchWeatherData();
  }, []);

  const handleAlertSubmit = async (e) => {
    e.preventDefault();
    if (!threshold || isNaN(threshold)) {
      alert("Please enter a valid numeric threshold.");
      return;
    }

    try {
      // POST request with query parameters in the URL
      const response = await axios.post(
        `http://localhost:8090/setAlert`,
        null,
        {
          params: { city: selectedCity, threshold: Number(threshold) },
        }
      );

      setAlertResponse(`Alert set successfully`);
    } catch (error) {
      console.error("Error setting alert:", error);
      setAlertResponse("Failed to set alert.");
    }
  };

  const renderWeatherInfo = () => {
    const cityWeather = weatherData[selectedCity];

    if (!cityWeather) return <p>Loading...</p>;

    return (
      <div>
        <h3>{selectedCity} Weather</h3>
        <p>Dominant Condition: {cityWeather.dominantCondition}</p>
        <p>Average Temperature: {cityWeather.avgTemp}°C</p>
        <p>Max Temperature: {cityWeather.maxTemp}°C</p>
        <p>Min Temperature: {cityWeather.minTemp}°C</p>
        <p>Last Updated: {new Date(cityWeather.timestamp).toLocaleString()}</p>
      </div>
    );
  };

  return (
    <div className="main">
      <h2>Weather Dashboard</h2>
      <div className="tabs">
        {cities.map((city) => (
          <button
            key={city}
            onClick={() => setSelectedCity(city)}
            className={selectedCity === city ? "active" : ""}
          >
            {city}
          </button>
        ))}
      </div>
      <div className="weather-info">
        <WeatherChart city={selectedCity} />
        {renderWeatherInfo()}
      </div>

      <div className="alert-form">
        <h3>Set Temperature Alert for {selectedCity}</h3>
        <form onSubmit={handleAlertSubmit}>
          <label>
            Temperature Threshold (°C):
            <input
              type="number"
              value={threshold}
              onChange={(e) => setThreshold(e.target.value)}
              placeholder="Enter threshold"
            />
          </label>
          <button type="submit">Set Alert</button>
        </form>
        {alertResponse && <p>{alertResponse}</p>}
      </div>
    </div>
  );
};

export default App;
