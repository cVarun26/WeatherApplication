import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import "chart.js/auto";
import axios from "axios";

const WeatherChart = ({ city }) => {
  const [chartData, setChartData] = useState({});
  const [dailyData, setDailyData] = useState({});

  const fetchWeatherData = async () => {
    try {
      const response = await axios(`http://localhost:8090/viz/${city}`);
      const data = response.data;

      if (
        Array.isArray(data.avgTemps) &&
        Array.isArray(data.timestamps) &&
        data.avgTemps.length > 0 &&
        data.timestamps.length > 0
      ) {
        const avgTemps = data.avgTemps;
        const timestamps = data.timestamps.map((timestamp) => {
          const date = new Date(timestamp);
          return date.toLocaleDateString(); 
        });

        const tempByDate = {};

        timestamps.forEach((date, index) => {
          if (!tempByDate[date]) {
            tempByDate[date] = [];
          }
          tempByDate[date].push(parseFloat(avgTemps[index]));
        });

        const dailyAverages = {};
        for (const date in tempByDate) {
          const temps = tempByDate[date];
          const averageTemp =
            temps.reduce((sum, temp) => sum + temp, 0) / temps.length;
          dailyAverages[date] = averageTemp;
        }

        const sortedDates = Object.keys(dailyAverages).sort(
          (a, b) => new Date(a) - new Date(b)
        );

        const sortedAverages = sortedDates.map((date) => dailyAverages[date]);

        setDailyData(dailyAverages);

        setChartData({
          labels: sortedDates, // Sorted Dates
          datasets: [
            {
              label: `Daily Average Temperature (°C) for ${city}`,
              data: sortedAverages, 
              backgroundColor: "rgba(75, 192, 192, 0.6)",
              borderColor: "rgba(75, 192, 192, 1)",
              borderWidth: 1,
            },
          ],
        });
      } else {
        console.error(
          "avgTemps or timestamps are missing or invalid in the response data"
        );
      }
    } catch (error) {
      console.error("Error fetching weather data:", error);
    }
  };

  useEffect(() => {
    fetchWeatherData();

    const interval = setInterval(() => {
      fetchWeatherData();
    }, 300000); // 5 minutes

    return () => clearInterval(interval); 
  }, [city]);

  return (
    <div className="chart">
      <h2>Daily Weather Data for {city}</h2>
      {Object.keys(chartData).length > 0 ? (
        <Bar data={chartData} />
      ) : (
        <p>Loading chart...</p>
      )}
    </div>
  );
};

export default WeatherChart;
