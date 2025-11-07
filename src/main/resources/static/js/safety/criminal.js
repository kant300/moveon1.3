// ======== 전역변수 선언============== //
let map; // 지도 객체
let circle = null; // 2km 반경 원
let clickMarker = null; // 클릭, 검색시 생성되는 마커
let myMarker = null; // 나의 현재 위치 마커
let myInfoWindow = null; // 나의 현위치 인포윈도우
let criminalMarkers = []; // 성범죄자 마커들 배열에 모든 성범죄자 마커저장 -> 기존 마커 제거용이
// let myLocation = null; // 나의 현재위치 좌표

// ========= 지도 기본옵션============== //
const mapContainer = document.querySelector('#map'); // 지도를 표사할DOM
  const mapOption = {
    center: new kakao.maps.LatLng(37.4123326, 126.6878251), // 지도의 중심좌표 (원인재역)
    level: 3 // 지도의 확대레벨
  };

map = new kakao.maps.Map(mapContainer, mapOption); // 지도생성

var initialPosition; // 현재 위치를 저장할 변수


// 1. 카카오맵 초기화
const createMap = async () => {
  // 지도 클릭 이벤트
  kakao.maps.event.addListener(map, 'click', async (mouseEvent) => {
    if (clickMarker) {
      clickMarker.setMap(null); // 기존 마커제거
    }
    // 클릭한 좌표 가져오기
    const latlng = mouseEvent.latLng;
    const clickLocation = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng());
    
    // 새로운 클릭 마커 생성
    clickMarker = new kakao.maps.Marker({
      position: clickLocation,
      map: map
    });
    // 기존 원 제거
    if (circle) { 
      circle.setMap(null); 
    }

    // 2km 반경의 원 생성
    circle = new kakao.maps.Circle({
      center: clickLocation,
      radius: 2000,
      strokeWeight: 5,
      strokeColor: '#75B8FA',
      strokeOpacity: 0.8,
      strokeStyle: 'dashed',
      fillColor: '#CFE7FF',
      fillOpacity: 0.5
    });
    circle.setMap(map);

    // 지도 중심 이동 & 확대
    map.setCenter(clickLocation);
    map.setLevel(5);
    // 성범죄자 데이터 로드
    await loadCriminals(map, clickLocation);
  }); // 지도 클릭이벤트 end

  // 2. 사용자의 현재 위치 가져오기
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      async position => {
        const myLat = position.coords.latitude;
        const myLon = position.coords.longitude;
        const myLocation = new kakao.maps.LatLng(myLat, myLon);
        // 현재위치 마커생성
        myMarker = new kakao.maps.Marker({
          position: myLocation,
          map: map
        });
        myMarker.setMap(map);

        // 사용자의 현재위치 인포윈도우 
        myInfoWindow = new kakao.maps.InfoWindow( {
            map:map,
            position:myLocation,
            content: `<div style="padding:3px; font-family: 'NanumGothic';">현재 위치입니다.</div>`,
            removable : true,
            zIndex: 100 // 마커보다 높은 값으로 설정
        } );
        myInfoWindow.open(map, myMarker);

        // 현재 위치 데이터를 전역 변수에 저장(복귀용)
        initialPosition = myLocation;

        // 원제거 후 다시 그림
        if (circle) { circle.setMap(null); }// 원삭제
        // clearMarkers();

        // 2km 반경의 원 생성
        circle = new kakao.maps.Circle({
          center: myLocation,
          radius: 2000,
          strokeWeight: 5,
          strokeColor: '#75B8FA',
          strokeOpacity: 0.8,
          strokeStyle: 'dashed',
          fillColor: '#CFE7FF',
          fillOpacity: 0.5
        });
        circle.setMap(map);

        map.setCenter(myLocation);
        map.setLevel(5);
        // 성범죄자 마커 로드
        await loadCriminals(map, myLocation);
      }, // 현재 위치 가져오기 성공 콜백 끝나는 지점
      error => {
        console.error('위치 정보를 가져오는 데 실패했습니다.', error);
        alert('위치 정보를 가져올 수 없어 기본 위치로 지도를 표시합니다.');
        loadCriminals(map, null);
      }// 현재 위치 가져오기 실패 콜백 end
    );
  } else {
    alert('이 브라우저는 위치 정보를 지원하지 않습니다.');
    loadCriminals(map, null);
  }
}; // createMap end

createMap();

// 범죄자 데이터를 로드하고 마커를 그리는 함수
const loadCriminals = async (map, myLocation) => {
  // 기존 마커 제거
  if (criminalMarkers.length > 0) {
    for (let i = 0; i < criminalMarkers.length; i++) {
      criminalMarkers[i].setMap(null);
    }
    criminalMarkers = [];
  }
  // 서버에서 범죄자 목록 가져오기
  const response = await fetch("/safety/criminal", { method: "GET" });
  const criminalList = await response.json();
  console.log(criminalList);

  let criminalCount = 0;

  for (let i = 0; i < criminalList.length; i++) {
    const criminal = criminalList[i];
    const criminalLocation = new kakao.maps.LatLng(criminal.latitude, criminal.longitude);

    // 마커이미지 설정(빨간색 마커)
    const imageSrc = "/img/마커_red.png";
    const imageSize = new kakao.maps.Size(25, 34);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    if (myLocation) { // 거리계산
      const linePath = [myLocation, criminalLocation];
      const polyline = new kakao.maps.Polyline({ path: linePath });
      const distance = polyline.getLength();
      // 2km이내만 표시
      if (distance <= 2000) {
        criminalCount++;

        const marker = new kakao.maps.Marker({
          map: map,
          position: criminalLocation,
          image: markerImage
        });

        criminalMarkers.push(marker);

        // 인포 윈도우(주소표시)
        const iwContent = `
          <div style="padding:5px;font-size:12px;max-width:300px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;"> 
            ${criminal.cAddress}
          </div>`;
        const infowindow = new kakao.maps.InfoWindow({
          content: iwContent,
          removable: true
        });

        kakao.maps.event.addListener(marker, 'click', function () {
          infowindow.open(map, marker);
        });
      }
    }
  }
  // HTML에 성범죄자 수 표시
  document.getElementById('criminal_count').innerText = criminalCount;
}; // LoadCriminals end

// 검색 실행 함수
const searchAddressOrPlace = (keyword) => {
  if (clickMarker) clickMarker.setMap(null);

  const geocoder = new kakao.maps.services.Geocoder();
  const places = new kakao.maps.services.Places();

  // 주소 검색 시도
  geocoder.addressSearch(keyword, async (result, status) => {
    if (status === kakao.maps.services.Status.OK && result.length > 0) {
      const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
      setSearchMarker(coords);
    } else {
      // 주소 검색 실패 → 장소 키워드 검색
      places.keywordSearch(keyword, async (data, status) => {
        if (status === kakao.maps.services.Status.OK && data.length > 0) {
          const coords = new kakao.maps.LatLng(data[0].y, data[0].x);
          setSearchMarker(coords);
        } else {
          alert("검색 결과가 없습니다.");
        }
      });
    }
  });
}; // searchAddressOrPlace end

// 검색결과 마커찍기
const setSearchMarker = async (latlng) => {
  if (clickMarker != null) 
    clickMarker.setMap(null);

  // console.log(clickMarker)
  clickMarker = new kakao.maps.Marker({
    position: latlng,
    map: map
  });

  if (circle) circle.setMap(null);

  circle = new kakao.maps.Circle({
    center: latlng,
    radius: 2000,
    strokeWeight: 5,
    strokeColor: '#75B8FA',
    strokeOpacity: 0.8,
    strokeStyle: 'dashed',
    fillColor: '#CFE7FF',
    fillOpacity: 0.5
  });
  circle.setMap(map);

  map.setCenter(latlng);
  map.setLevel(5);

  await loadCriminals(map, latlng);
}; // setSearchMarker end

// 버튼 클릭이벤트
document.querySelector('.box_search button').addEventListener('click',() => {
  const keyword = document.getElementById('innerQuery').value.trim();
  if(!keyword){
    alert("검색어를 입력하세요");
    return;
  }
  searchAddressOrPlace(keyword);
} ); // 버튼클릭이벤트 end

// 엔터키 이벤트
document.querySelector('.tf_keyword').addEventListener('keydown', (event) => {
  //console.log(event);
    if (event.key == "Enter") {
    event.preventDefault(); // 기본 엔터 동작 막기
    const keyword = event.target.value.trim();
    if (!keyword) {
      alert("검색어를 입력하세요.");
      return;
    }
    searchAddressOrPlace(keyword);
  }
}); // 엔터키 이벤트 end

// 현재 위치로 이동
const moveToInitialPosition = () => {
    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(initialPosition);      
} // moveToInitialPosition end