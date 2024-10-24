# Real-Time Weather Monitoring and Alert System

## Project Overview
This project is a real-time data processing system that monitors weather conditions for major metros in India and provides summarized insights using rollups and aggregates. The backend fetches weather data from the [OpenWeatherMap API](https://openweathermap.org/), processes it, and stores daily summaries in a database. Users can also set configurable thresholds for alerts based on weather conditions.

## Key Features
1. **Real-Time Weather Monitoring**: The system continuously fetches weather data at configurable intervals (e.g., every 5 minutes) for cities like Delhi, Mumbai, Bangalore, and more.
2. **Daily Weather Summaries**: Aggregates are generated daily, including average, maximum, and minimum temperatures, along with the dominant weather condition.
3. **Custom Alert Thresholds**: Users can set temperature thresholds, and the system will trigger alerts if breached.
4. **Visualizations**: The frontend, built with React.js, displays weather trends and alerts through interactive charts and data summaries.

## Technologies Used
- **Backend**: Java, Spring Boot
- **Frontend**: React.js
- **Database**: MySQL
- **API**: OpenWeatherMap API
- **Visualization**: Chart.js (for graphical data representation)

 ## Design Choices

### Backend
- *Framework*: Spring Boot for its scalability, flexibility, and ease of integration with MySQL and external APIs.
- *API Integration*: OpenWeatherMap API provides weather data that is fetched and processed by the backend every 5 minutes (or as configured).
- *Data Processing*: Temperatures are converted from Kelvin to Celsius (or Fahrenheit based on user preferences), and daily summaries are computed by aggregating data over time.
- *Alerting*: Alerts are set by users and triggered when real-time weather data exceeds configured thresholds.
- *Persistence*: MySQL is used for storing historical weather data, daily summaries, and user-configured thresholds.
  
### Frontend
- *Framework*: React.js was chosen for building an interactive and dynamic UI.
- *Visualization*: Chart.js is used to create weather data visualizations, including temperature trends and alerts.
- *API Consumption*: The frontend consumes RESTful APIs exposed by the backend to display real-time weather data and manage alerts.

## System Components

### Backend (Spring Boot)
1. **Weather Data Retrieval**: 
   - Retrieves weather data from the OpenWeatherMap API for multiple cities.
   - Converts temperature from Kelvin to Celsius or Fahrenheit (based on user preference).
   - Stores weather data in a MySQL database.

2. **Data Aggregation**:
   - Generates daily summaries including:
     - Average temperature
     - Maximum temperature
     - Minimum temperature
     - Dominant weather condition (calculated using the most frequent condition during the day).
  
3. **Alert System**:
   - Users can configure thresholds (e.g., alert if temperature exceeds 35°C).
   - System continuously monitors weather conditions and triggers alerts when thresholds are breached.

4. **API Endpoints**:
   - `/viz/{city}`: Collects data for visualisation based on the average temperature of the city.
   - `/setAlert`: Sets alert configurations and checks against real-time data.
   - `/weather`: Provides daily weather summary.

### Frontend (React.js)
1. **Dashboard**:
   - Displays real-time and historical weather data.
   - Uses Chart.js to visualize daily weather summaries (average, max, min temperatures).
   
2. **Alert Configurations**:
   - User interface to set alert thresholds for temperature.
   - Displays triggered alerts based on configured thresholds.

### Database (MySQL)
The MySQL database stores:
- Weather data (timestamp, temperature, condition, etc.)
- Daily summaries (average, max, min temperature, dominant condition)
- User-configured alert thresholds

## Setup Instructions

### Prerequisites
- Java 11+
- Node.js (for React frontend)
- MySQL database
- OpenWeatherMap API key

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository.git
   cd your-repository/backend
   ```

2. Update the `application.properties` file with your MySQL credentials and OpenWeatherMap API key:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/weather_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   openweathermap.api.key=your_openweathermap_api_key
   ```

3. Build and run the backend application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the React application:
   ```bash
   npm start
   ```

### Database Setup
1. Create a MySQL database:
   ```sql
   CREATE DATABASE weather_db;
   ```
   
2. The application will automatically create the required tables on startup.

## Test Cases

1. **System Setup**:
   - Ensure the application connects to the OpenWeatherMap API using the API key.
   
2. **Weather Data Retrieval**:
   - Simulate API calls at intervals and ensure data is correctly fetched and parsed.
   
3. **Temperature Conversion**:
   - Verify the system correctly converts temperatures from Kelvin to Celsius/Fahrenheit.
   
4. **Daily Weather Summary**:
   - Test if daily summaries (average, max, min temperatures) are correctly computed.
   
5. **Alert Thresholds**:
   - Set user-configurable thresholds and simulate threshold breaches to verify alerts.

## Alerting system

 - Configured a an alerting system using the JavaMailSender annotation which sends an email to a pre-existing email account whenever the user configured temperature threshold is breached in the respectively configured city.
 - The credentials for the pre-exisiting :-
   email - johndoe260703@gmail.com
   password - johndoe1234

### MAKE SURE TO RUN THE BACKEND WHILE RUNNING THE REACT APP
