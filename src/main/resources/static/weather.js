const apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Daejeon&lang=kr&appid=c91e22b768bbc74e94460d26396a2d5f"

// HTML 요소를 가져옵니다.
const temperatureElement = document.getElementById("temperature");
const weatherDescriptionElement = document.getElementById("weather-description");
const weatherIconElement = document.getElementById("weather-icon");

// API 요청을 보내고 응답을 처리합니다.
fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
        const weatherDescription = data.weather[0].description;
        const temperatureKelvin = data.main.temp;

        const temperatureCelsius = (temperatureKelvin - 273.15).toFixed(1);

        temperatureElement.textContent = `${temperatureCelsius}°C`;
        weatherDescriptionElement.textContent = `${weatherDescription}`;

        const iconCode = data.weather[0].icon;
        const iconUrl = `https://openweathermap.org/img/wn/${iconCode}.png`;
        const iconImage = document.createElement("img");
        iconImage.src = iconUrl;
        weatherIconElement.appendChild(iconImage);
    })
    .catch(error => {
        console.error("날씨 정보를 가져오는 중 오류가 발생했습니다.", error);
    });

    const currentDateElement = document.getElementById("current-date");


    const currentDate = new Date();


    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더합니다.
    const day = currentDate.getDate();

    const formattedDate = `${year}년 ${month}월 ${day}일`;

    currentDateElement.textContent = `${formattedDate}`;

    const currentTimeElement = document.getElementById("current-time");

    function displayCurrentTime() {
        const currentTime = new Date();
        const hours = currentTime.getHours().toString().padStart(2, '0');
        const minutes = currentTime.getMinutes().toString().padStart(2, '0');
        const seconds = currentTime.getSeconds().toString().padStart(2, '0');
        const formattedTime = `${hours}:${minutes}:${seconds}`;
        currentTimeElement.textContent = `${formattedTime}`;
    }

    // 초기에 한 번 시간을 표시합니다.
    displayCurrentTime();

    // 1초마다 현재 시간을 업데이트합니다.
    setInterval(displayCurrentTime, 1000);