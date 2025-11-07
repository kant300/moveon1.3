
const displayWeather = async () => {
    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
    if (navigator.geolocation) {
    
    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(async function(position) {
        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        // 필요한 시간대 구하기
        const now = new Date();
        let hour = String(now.getHours()).padStart(2, '0');
        
        try {
            // 날씨 데이터 가져오기
            const response = await fetch(`/weather?lat=${parseInt(lat)}&lon=${parseInt(lon)}`);
            const data = await response.json();

            // 데이터가 없으면 콘솔과 HTML에 코드 출력 후 리턴
            if (data.response.header.resultCode != "00") {
                console.log(data.response.header.resultCode);
                console.log(data.response.header.resultMsg);
                weather.innerHTML = 'NO DATA';
                return;                
            }

            // 좌표로 주소 구하기
            var geocoder = new kakao.maps.services.Geocoder();
            var coord = new kakao.maps.LatLng(lat, lon);
            var callback = function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    // 주소 (예시 : 인천 부평구 부평동)
                    let addr = result[0].address.region_1depth_name + " " + result[0].address.region_2depth_name + " " + result[0].address.region_3depth_name;

                    // 필요한 데이터 가져오기
                    hour += '00';
                    let t1h, reh, pty, sky, wsd;
                    for (let i=0; i<data.response.body.items.item.length; i++) {
                        let obj = data.response.body.items.item[i];
                        if (hour == obj.fcstTime) {
                            if (obj.category == "T1H") {
                                t1h = obj.fcstValue; // 기온
                            }
                            if (obj.category == "REH") {
                                reh = obj.fcstValue; // 습도
                            }
                            if (obj.category == "PTY") {
                                pty = obj.fcstValue; // 강수형태
                                if (pty == 0) pty = "맑음";
                                if (pty == 1) pty = "비";
                                if (pty == 2) pty = "비/눈";
                                if (pty == 3) pty = "눈";
                                if (pty == 4) pty = "소나기";
                            }
                            if (obj.category == "SKY") {
                                sky = obj.fcstValue; // 하늘상태
                                if (sky == 1) sky = "맑음";
                                if (sky == 3) sky = "구름많음";
                                if (sky == 4) sky = "흐림";
                            }
                            if (obj.category == "WSD") {
                                wsd = obj.fcstValue; // 풍속
                            }
                        }
                    }
                    hour = hour.slice(0, 2);

                    // 날씨에 따른 아이콘 그리기
                    let icon;
                    if (pty == "맑음" && sky == "맑음") icon = "☀️";
                    else if (sky == "구름많음") icon = "⛅️";
                    else if (sky == "흐림") icon = "☁️";
                    else if (pty == "비" || pty == "비/눈" || pty == "소나기") icon = "🌧";
                    else if (pty == "눈") icon = "🌨";

                    // HTML에 그리기
                    const weather = document.querySelector(".weather");
                    let html = `<div class="addr"><strong>${addr}</strong>의 날씨 (${hour}시 기준)</div>
                                    <div class="t1h">${icon} ${t1h}° ${pty}</div>
                                    

                                    <div class="weatherDetails">
                                        <div class="item">
                                            <span class="label">습도</span>
                                            <span>${reh}%</span>
                                        </div>
                                        <div class="item">
                                            <span class="label">하늘</span>
                                            <span>${sky}</span>
                                        </div>
                                        <div class="item">
                                            <span class="label">풍속</span>
                                            <span>${wsd}m/s</span>
                                        </div>
                                    </div>
                                </div>`;

                    weather.innerHTML = html;
                }
            };
            geocoder.coord2Address(coord.getLng(), coord.getLat(), callback);
        } catch (error) {
            console.log(error);
        }});
    } else { // HTML5의 GeoLocation을 사용할 수 없을때 내용을 설정합니다
        console.log("Geolocation을 사용할 수 없습니다.");
    }
}
displayWeather();