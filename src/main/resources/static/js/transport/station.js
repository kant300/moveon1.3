var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
mapOption = { 
    center: new kakao.maps.LatLng(37.4066562, 126.6286125), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
};

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

var initialPosition;

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
            initialPosition = locPosition;
            
            // 마커와 인포윈도우를 표시합니다
            displayMarker(locPosition, message);
                
        });
        
    } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
        
        var locPosition = new kakao.maps.LatLng(37.4066562, 126.6286125),    
            message = 'geolocation을 사용할 수 없는 상태입니다.'
            
        displayMarker(locPosition, message);
    }

    // 데이터를 가져와 필요한 데이터를 삽입합니다
    // 1. 매핑된 데이터를 가져옵니다
    const response = await fetch("/station/data", {method : "GET"});
    const data = await response.json();
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
        map: map, // 클러스터러에 표시할 지도 객체
        averageCenter: true, // 클러스터 마커의 위치를 클러스터된 마커들의 평균 위치로 설정
        minLevel: 4 // 클러스터링이 실행될 최소 지도 레벨
    });
    var markers = [];
        
    // 엘리베이터, 에스컬레이터 마커 찍기
    for (let i = 0; i < positions.length; i ++) {
        const obj = positions[i];

        // 마커 이미지의 이미지 주소입니다
        var imageSrc = obj.status=="수리중"?"/img/마커_red.png":"/img/마커.png";
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
        let iwContent = `<div style="width:150px;text-align:center;padding:6px 0;padding-top:18px;font-family: 'NanumGothic';">${obj.title}역의 ${obj.unit}호 ${obj.equipment}는 ${obj.status}입니다.</div>` // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        var iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

        // 인포윈도우를 생성합니다
        let infowindow = new kakao.maps.InfoWindow({
            content : iwContent,
            removable : iwRemoveable
        });

        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'click', function() {
            // 마커 위에 인포윈도우를 표시합니다
            infowindow.open(map, marker);  
        });

        markers.push(marker);
    }
    clusterer.addMarkers(markers);

    // 지도에 마커와 인포윈도우를 표시하는 함수입니다
    function displayMarker(locPosition, message) {

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({  
            map: map, 
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
        infowindow.open(map, marker);
        
        // 지도 중심좌표를 접속위치로 변경합니다
        map.setCenter(locPosition);      
    }
}
createMap();

const moveToInitialPosition = () => {
    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(initialPosition);      
}