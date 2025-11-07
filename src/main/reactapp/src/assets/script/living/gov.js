import axios from "axios"
import markerIcon from "../../images/icons/marker.png"

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
                    navigator.geolocation.getCurrentPosition(function (position) {

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

                const getGovernmentOfficeData = async () => {
                    // 승강기 데이터를 가져와 필요한 데이터를 삽입합니다
                    // 1. 매핑된 데이터를 가져옵니다
                    const response = await axios.get("http://localhost:8080/living/gov");
                    const data = await response.data;
                    var positions = [];
                    for (let i = 0; i < data.length; i++) {
                        const obj = data[i];
                        positions.push({
                            유형: obj.유형,
                            시설명: obj.시설명,
                            주소: obj.주소,
                            전화번호: obj.전화번호,
                            latlng: new kakao.maps.LatLng(obj.위도, obj.경도)
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
                        var imageSrc = markerIcon;
                        // 마커 이미지의 이미지 크기 입니다
                        var imageSize = new kakao.maps.Size(25, 34);
                        // var imageOption = {offset: new kakao.maps.Point(50, -60)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

                        // 마커 이미지를 생성합니다    
                        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

                        // 마커를 생성합니다
                        let marker = new kakao.maps.Marker({
                            position: obj.latlng, // 마커를 표시할 위치
                            title: obj.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                            image: markerImage // 마커 이미지 
                        });

                        // 3. positions 배열에 삽입한 데이터를 꺼내옵니다
                        let iwContent = `<div style="width:400px;text-align:center;padding:10px;font-family: 'NanumGothic';">
                            <div>${obj.시설명}</div>
                            <div>${obj.주소}</div>
                            <div>${obj.전화번호}</div>
                        ` // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                        var iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

                        // 인포윈도우를 생성합니다
                        let infowindow = new kakao.maps.InfoWindow({
                            content: iwContent,
                            removable: iwRemoveable
                        });

                        // 마커에 클릭이벤트를 등록합니다
                        kakao.maps.event.addListener(marker, 'click', function () {
                            // 마커 위에 인포윈도우를 표시합니다
                            infowindow.open(mapRef.current, marker);
                        });
                        markers.push(marker);
                    }
                    clusterer.addMarkers(markers);
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
                        content: iwContent,
                        removable: iwRemoveable
                    });

                    // 인포윈도우를 마커위에 표시합니다 
                    infowindow.open(mapRef.current, marker);

                    // 지도 중심좌표를 접속위치로 변경합니다
                    mapRef.current.setCenter(locPosition);
                }
                // 필요한 데이터를 가져옵니다
                getGovernmentOfficeData();
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