const WEATHER_API_KEY = 'c7160f62e83b4c5887514525241110';

// Function to get the current weather
async function getWeather(latitude, longitude) {
  try {
    const response = await fetch(`https://api.weatherapi.com/v1/current.json?key=${WEATHER_API_KEY}&q=${latitude},${longitude}&aqi=no`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return {
      condition: data.current.condition.text,
      tempC: data.current.temp_c,
      tempF: data.current.temp_f,
      icon: data.current.condition.icon,
      location: `${data.location.name}, ${data.location.region}`
    };
  } catch (error) {
    console.error('Error fetching weather data:', error);
    throw new Error('Failed to fetch weather data. Please try again.');
  }
}

// Function to get user's location with persistent permission
function getUserLocation() {
  return new Promise((resolve, reject) => {
    if ("geolocation" in navigator) {
      const options = {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0
      };

      navigator.permissions.query({ name: 'geolocation' }).then((permissionStatus) => {
        permissionStatus.onchange = () => {
          console.log('Geolocation permission status has changed to:', permissionStatus.state);
        };

        if (permissionStatus.state === 'granted') {
          navigator.geolocation.getCurrentPosition(successCallback, errorCallback, options);
        } else if (permissionStatus.state === 'prompt') {
          navigator.geolocation.getCurrentPosition(successCallback, errorCallback, options);
        } else {
          reject(new Error("Geolocation permission denied. Please enable location access in your browser settings."));
        }
      });

      function successCallback(position) {
        resolve({ latitude: position.coords.latitude, longitude: position.coords.longitude });
      }

      function errorCallback(error) {
        console.error('Error getting user location:', error);
        reject(new Error("Unable to retrieve your location. Please check your browser settings."));
      }
    } else {
      reject(new Error("Geolocation is not supported by this browser."));
    }
  });
}

// Function to validate zip code
function isValidZipCode(zipCode) {
  const zipRegex = /^\d{5}(-\d{4})?$/;
  return zipRegex.test(zipCode);
}

// Main function to update the popup
async function updatePopup() {
  const weatherDiv = document.getElementById('weather');
  const locationInput = document.getElementById('locationInput');

  weatherDiv.textContent = 'Fetching weather data...';

  try {
    let latitude, longitude;

    if (locationInput.value.trim()) {
      const zipCode = locationInput.value.trim();
      if (!isValidZipCode(zipCode)) {
        throw new Error('Invalid ZIP code. Please enter a valid 5-digit ZIP code.');
      }
      const response = await fetch(`https://api.weatherapi.com/v1/current.json?key=${WEATHER_API_KEY}&q=${zipCode}&aqi=no`);
      if (!response.ok) {
        throw new Error(`Invalid ZIP code or API error. Status: ${response.status}`);
      }
      const data = await response.json();
      latitude = data.location.lat;
      longitude = data.location.lon;
    } else {
      const location = await getUserLocation();
      latitude = location.latitude;
      longitude = location.longitude;
    }

    const weather = await getWeather(latitude, longitude);

    weatherDiv.innerHTML = `
      <h2>${weather.location}</h2>
      <p>${weather.condition}</p>
      <p>Temperature: ${weather.tempC}°C / ${weather.tempF}°F</p>
      <img src="${weather.icon}" alt="${weather.condition}" />
    `;
  } catch (error) {
    weatherDiv.textContent = `Error: ${error.message}`;
    console.error('Error in updatePopup:', error);
  }
}

// Add event listener for the refresh button
document.getElementById('refreshButton').addEventListener('click', updatePopup);

// Add event listener for the location input
document.getElementById('locationInput').addEventListener('keypress', function(e) {
  if (e.key === 'Enter') {
    updatePopup();
  }
});

// Initial update
document.addEventListener('DOMContentLoaded', updatePopup);