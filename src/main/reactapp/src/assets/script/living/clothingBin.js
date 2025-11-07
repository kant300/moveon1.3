import markerIcon from "../../images/icons/marker.png"

export function Run(mapRef, pos) {
    const script = document.createElement('script');
    script.src = '//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&autoload=false&libraries=services,clusterer';
    script.async = true;
    script.onload = () => {
        window.kakao.maps.load(() => {
            var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
                center: new kakao.maps.LatLng(37.4178, 126.67897), // 지도의 중심좌표
                level: 9 // 지도의 확대 레벨
            });

            mapRef.current = map;

            // [1] 카카오맵 함수
            const kakaomap = async () => {
                // 1.지도를 표시할 div

                // 2. 사용자의 현재 위치 가져오기
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(
                        async position => {
                            const lat = position.coords.latitude;
                            const lon = position.coords.longitude;
                            const myLocation = new kakao.maps.LatLng(lat, lon);

                            // 현재 위치 데이터를 전역 변수에 저장합니다.
                            pos.current = myLocation;

                            var marker = new kakao.maps.Marker({
                                position: myLocation,
                                map: map
                            });
                            marker.setMap(map);
                            // 2-1. 사용자의 현재 위치 인포윈도우 표시
                            var infowindow = new kakao.maps.InfoWindow({
                                map: map,
                                position: myLocation,
                                content: `<div style="padding:5px;font-family: 'NanumGothic';">현재 위치입니다.</div>`,
                                removable: true
                            });

                            infowindow.open(map, marker);
                        });
                };

                // 3. 마커 클러스터 생성
                var clusterer = new kakao.maps.MarkerClusterer({
                    map: map,
                    averageCenter: true,
                    minlevel: 4,
                    disableClickZoom: true
                });

                // 4. FETCH 이용한 공공데이터 자료 활용
                const URL = 'https://api.odcloud.kr/api/15141554/v1/uddi:574fcc84-bcb8-4f09-9588-9b820731bf19?page=1&perPage=368&serviceKey=lxvZMQzViYP1QmBRI9MrdDw5ZmsblpCAd5iEKcTRES4ZcynJhQxzAuydpechK3TJCn43OJmweWMoYZ10aspdgQ%3D%3D'
                const response = await fetch(URL, { method: "GET" });
                const data = await response.json();
                console.log(data); // 의류수거함 정보

                // 5. 반복문을 이용하여 마커를 하나씩 생성하여 retrun 한 마커를 makers 변수에 대입한다.
                let markers = data.data.map((value) => {
                    // 마커 이미지의 주소 입니다.
                    var imageSrc = markerIcon;
                    // 마커 이미지의 크기 입니다.
                    var imageSize = new kakao.maps.Size(25, 34);
                    // 마커 이미지 생성합니다.
                    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
                    // 마커 생성
                    let marker = new kakao.maps.Marker({
                        position: new kakao.maps.LatLng(value.위도, value.경도),
                        image: markerImage
                    });

                    marker.setMap(map);

                    // 6. positions 배열에 삽입한 데이터를 꺼내옵니다
                    kakao.maps.event.addListener(marker, 'click', () => {
                        var iwContent = `<div style="width:200px;text-align:center;padding:6px 0;padding-top:18px;font-family: 'NanumGothic';">${value["도로명 주소"]}</div>`,
                            iwRemoveable = true;

                        // 인포윈도우를 생성합니다
                        var infowindow = new kakao.maps.InfoWindow({
                            content: iwContent,
                            removable: iwRemoveable
                        });
                        // 마커에 클릭이벤트를 등록합니다
                        kakao.maps.event.addListener(marker, 'click', function () {
                            // 마커 위에 인포윈도우를 표시합니다
                            infowindow.open(map, marker);
                        });
                    })// 마커 리턴
                    return marker;
                });
                // 7. 여러개의 마커를 가진 markers 변수를 클러스터에 등록
                clusterer.addMarkers(markers);
                // 8. 마커 클러스터에 클릭이벤트를 등록
                kakao.maps.event.addListener(clusterer, 'clusterclick', function (cluster) {
                    // 현재 지도 레벨에서 1레벨 확대한 레벨
                    var level = map.getLevel() - 1;
                    // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대
                    map.setLevel(level, { anchor: cluster.getCenter() });
                });
            }
            kakaomap();
        });
    };
    document.head.appendChild(script);
}

export function moveToInitialPosition(mapRef, pos) {
    // 지도 중심좌표를 접속위치로 변경합니다
    mapRef.current.setCenter(pos.current);
}