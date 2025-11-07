import axios from "axios"
import markerIcon from "../../images/icons/marker.png"
import markerIcon_red from "../../images/icons/marker_red.png"
import markerIcon_station from "../../images/icons/marker_station.png"

export function Run(mapRef, pos) {
    const script = document.createElement('script');
    script.src = '//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&autoload=false&libraries=services,clusterer';
    script.async = true;
    script.onload = () => {
        window.kakao.maps.load(() => {
            const kakao = window.kakao;
            var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
            mapOption = { 
                center: new kakao.maps.LatLng(37.4066562, 126.6286125), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };
    
            mapRef.current = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
            var markers = [];
    
            const createMap = async () => {
                // 본인 위치 찍기
                // HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
                if (navigator.geolocation) {
                    
                    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
                    navigator.geolocation.getCurrentPosition(function(position) {
                        
                        var lat = position.coords.latitude, // 위도
                            lon = position.coords.longitude; // 경도
                        
                        var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
                            message = `<div style="padding:5px;font-family: 'NanumGothic';">현재 위치입니다.</div>`; // 인포윈도우에 표시될 내용입니다
    
                        // 현재 위치 데이터를 전역 변수에 저장합니다.
                        pos.current = locPosition;
                        
                        // 마커와 인포윈도우를 표시합니다
                        displayMarker(locPosition, message);
                        
                    });
                    
                } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
                    
                    var locPosition = new kakao.maps.LatLng(37.4066562, 126.6286125),    
                        message = 'geolocation을 사용할 수 없는 상태입니다.'
                        
                    displayMarker(locPosition, message);
                }

                const getLiftData = async () => {
                    // 승강기 데이터를 가져와 필요한 데이터를 삽입합니다
                    // 1. 매핑된 데이터를 가져옵니다
                    const response = await axios.get("http://localhost:8080/transport/lift");
                    const data = await response.data;
                    var positions = [];
                    for (let i=0; i<data.length; i++) {
                        const obj = data[i];
                        // 2. 매핑된 데이터를 삽입합니다 (키: 값)
                        positions.push({
                            title: obj.역사,
                            latlng: new kakao.maps.LatLng(obj.위도, obj.경도),
                            equipment: obj.장비,
                            unit: obj.호기,
                            status: obj.상태
                        });
                    }
        
                    // 클러스터러 적용
                    var clusterer = new kakao.maps.MarkerClusterer({
                        map: mapRef.current, // 클러스터러에 표시할 지도 객체
                        averageCenter: true, // 클러스터 마커의 위치를 클러스터된 마커들의 평균 위치로 설정
                        minLevel: 4 // 클러스터링이 실행될 최소 지도 레벨
                    });
                        
                    // 엘리베이터, 에스컬레이터 마커 찍기
                    for (let i = 0; i < positions.length; i++) {
                        const obj = positions[i];
        
                        // 마커 이미지의 이미지 주소입니다
                        var imageSrc = obj.status=="수리중"?markerIcon_red:markerIcon;
                        // 마커 이미지의 이미지 크기 입니다
                        var imageSize = new kakao.maps.Size(25, 34);
                        // var imageOption = {offset: new kakao.maps.Point(32, 9)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                        
                        // 마커 이미지를 생성합니다    
                        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
                        
                        // 마커를 생성합니다
                        let marker = new kakao.maps.Marker({
                            position: obj.latlng, // 마커를 표시할 위치
                            title : obj.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                            image : markerImage // 마커 이미지 
                        });
        
                        // 3. positions 배열에 삽입한 데이터를 꺼내옵니다
                        let iwContent = `<div style="width:200px;text-align:center;padding:10px;font-family: 'NanumGothic';"><div>${obj.title}역</div><div>${obj.unit}호 ${obj.equipment}</div><div>${obj.status}</div>` // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                        var iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
        
                        // 인포윈도우를 생성합니다
                        let infowindow = new kakao.maps.InfoWindow({
                            content : iwContent,
                            removable : iwRemoveable
                        });
        
                        // 마커에 클릭이벤트를 등록합니다
                        kakao.maps.event.addListener(marker, 'click', function() {
                            // 마커 위에 인포윈도우를 표시합니다
                            infowindow.open(mapRef.current, marker);
                        });
                        markers.push(marker);
                    }
                    clusterer.addMarkers(markers);
                }

                function parseTime(time) {
                    if (time == null) return 0;
                    // 'HH:mm:ss' 형식의 문자열을 분리하여 시, 분, 초를 추출
                    const [hours, minutes] = time.split(':').map(Number);

                    return hours * 3600000 + minutes * 60000;
                }

                const getStationData = async () => {
                    // 현재 시간 구하기
                    const time = new Date();
                    const hours = String(time.getHours()).padStart(2, '0');
                    const minutes = String(time.getMinutes()).padStart(2, '0');
                    const now = parseTime(`${hours}:${minutes}:00`);
                    
                    // 지하철 역사 위치 데이터를 가져와 필요한 데이터를 삽입합니다
                    // A-1. 매핑된 데이터를 가져옵니다
                    const st_response = await axios.get("http://localhost:8080/transport/location");
                    const st_data = await st_response.data;
                    var positions = [];
                    for (let i=0; i<st_data.length; i++) {
                        const obj = st_data[i];
                        // 이전 역, 다음 역 이름
                        const prevStation = i > 0 ? st_data[i-1].역사명 : null;
                        const nextStation = i < st_data.length-1 ? st_data[i+1].역사명 : null;

                        // 지하철 배차 정보 데이터를 가져와 필요한 데이터를 삽입합니다
                        // B-1. 매핑된 데이터를 가져옵니다
                        const sc_response = await axios.get("http://localhost:8080/transport/schedule?station_name="+obj.역사명);
                        const sc_data = await sc_response.data;

                        const time1 = parseTime(sc_data[0][0]);
                        const time2 = parseTime(sc_data[0][1]);
                        const time3 = parseTime(sc_data[1][0]);
                        const time4 = parseTime(sc_data[1][1]);

                        // n분 후 시간 구하기
                        const time_first_1 = time1-now;
                        const time_second_1 = time2-now;
                        const time_first_2 = time3-now;
                        const time_second_2 = time4-now;

                        // 2. 매핑된 데이터를 삽입합니다 (키: 값)
                        // 역사명, 위경도, 상선 1,2번 시간, 하선 1,2번 시간
                        positions.push({
                            title: obj.역사명,
                            latlng: new kakao.maps.LatLng(obj.위도, obj.경도),
                            time_first_1: time_first_1/1000/60,
                            time_second_1: time_second_1/1000/60,
                            time_first_2: time_first_2/1000/60,
                            time_second_2: time_second_2/1000/60,
                            prevStation,
                            nextStation
                        });
                    }
                    
                    // 지하철 역사 마커 찍기
                    for (let i = 0; i < positions.length; i++) {
                        const obj = positions[i];

                        // 마커 이미지의 이미지 주소입니다
                        var imageSrc = markerIcon_station;
                        // 마커 이미지의 이미지 크기 입니다
                        var imageSize = new kakao.maps.Size(37, 51);
                        // var imageOption = {offset: new kakao.maps.Point(32, 9)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                        
                        // 마커 이미지를 생성합니다    
                        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
                        
                        // 마커를 생성합니다
                        let marker = new kakao.maps.Marker({
                            position: obj.latlng, // 마커를 표시할 위치
                            title : obj.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                            image : markerImage // 마커 이미지 
                        });

                          // 지도에 표시
                          marker.setMap(mapRef.current);  // ✅ 추가

                        // 3. positions 배열에 삽입한 데이터를 꺼내옵니다
                        let iwContent = `
                            <div style="width:400px;text-align:center;padding:10px;font-family:'NanumGothic';">
                                <div><strong>${obj.title}역 배차 정보</strong></div>
                                ${obj.prevStation == null 
                                ? '' 
                                : (obj.time_first_1 < 0 || obj.time_second_1 < 0
                                    ? `<div>${obj.prevStation}역 방면: 배차 정보가 없습니다.</div>` 
                                    : `<div>${obj.prevStation}역 방면: ${obj.time_first_1}분후 도착, ${obj.time_second_1}분후 도착</div>`)}
                                ${obj.nextStation == null 
                                ? '' 
                                : (obj.time_first_2 < 0 || obj.time_second_2 < 0
                                    ? `<div>${obj.nextStation}역 방면: 배차 정보가 없습니다.</div>` 
                                    : `<div>${obj.nextStation}역 방면: ${obj.time_first_2}분후 도착, ${obj.time_second_2}분후 도착</div>`)}
                            </div>
                        `;
                            // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                        var iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

                        // 인포윈도우를 생성합니다
                        let infowindow = new kakao.maps.InfoWindow({
                            content : iwContent,
                            removable : iwRemoveable
                        });
        
                        // 마커에 클릭이벤트를 등록합니다
                        kakao.maps.event.addListener(marker, 'click', function() {
                            // 마커 위에 인포윈도우를 표시합니다
                            infowindow.open(mapRef.current, marker);
                        });
                        markers.push(marker);
                    }
                }
                
                // 지도에 마커와 인포윈도우를 표시하는 함수입니다
                function displayMarker(locPosition, message) {
    
                    // 마커를 생성합니다
                    var marker = new kakao.maps.Marker({  
                        map: mapRef.current, 
                        position: locPosition
                    }); 
                    
                    var iwContent = message, // 인포윈도우에 표시할 내용
                        iwRemoveable = true;
    
                    // 인포윈도우를 생성합니다
                    var infowindow = new kakao.maps.InfoWindow({
                        content : iwContent,
                        removable : iwRemoveable
                    });
                    
                    // 인포윈도우를 마커위에 표시합니다 
                    infowindow.open(mapRef.current, marker);
                    
                    // 지도 중심좌표를 접속위치로 변경합니다
                    mapRef.current.setCenter(locPosition);      
                }
                // 필요한 데이터를 가져옵니다
                getLiftData();
                getStationData();
            }
            createMap();
        });
    };
    document.head.appendChild(script);
}

export function moveToInitialPosition(mapRef, pos) {
    // 지도 중심좌표를 접속위치로 변경합니다
    mapRef.current.setCenter(pos.current);
}