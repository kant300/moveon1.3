
console.log('trash.js open');


// [1] 카카오맵 API을 이용하여 현재 위치좌표 => 현재 위치 지역 시,구 정보얻기 

// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
if (navigator.geolocation) {
    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function(position) {
        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도
            console.log(lat,lon);
            geocoder.coord2RegionCode(lon , lat, callback);
            // geocoder.coord2RegionCode( 126.723325411  , 37.489572382, callback); // 부평역 테스트
    });
}
// [2] 좌표를 주소로 변환, 화면에 출력
var geocoder = new kakao.maps.services.Geocoder();
var callback = function(result, status) {
    if (status === kakao.maps.services.Status.OK) {

        console.log(`지역 명칭 : ${result[0].region_1depth_name} ${result[0].region_2depth_name}`); 
        // region_1depth_name : 시도 단위 , region_2depth_name : 구 단위
        // https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-to-district // 추후 커스텀시 지역정보코드모음
        
        // textBox(jsp)에 현재 접속한 (시도단위, 구단위)위치 출력 
        document.querySelector('#textBox').innerHTML = `현재 접속 위치는 <strong>${result[0].region_1depth_name} ${result[0].region_2depth_name}</strong> 입니다.`;

        const 지역_시도 = result[0].region_1depth_name;
        console.log("지역시도 입니다. "+ 지역_시도 );   // 경기도 // 인천광역시

        const 지역_구 = result[0].region_2depth_name;
        console.log("지역구 입니다. "+ 지역_구 ); // 부천시 원미구 //

        // if 로 광역시 와 도(8) 구분해서
  

        trashFind( 지역_시도 , 지역_구 );
        trashMove( 지역_시도 , 지역_구 );
        // trashFind( 지역_시도+" "+지역_구.split(" ")[0]  , 지역_구.split(" ")[1] ); // : 쓰레기 개별 배출정보 호출 // 등록시 지역명들을  select 고민!!
        // trashMove( 지역_시도+" "+지역_구.split(" ")[0]  , 지역_구.split(" ")[1] );
    }
    
};

// [3] 쓰레기 개별 배출정보 출력
const trashFind = async ( tCity , tGu ) =>{
    try{ // 1. 어디에 // fetch로 부터 출력할 쓰레기 정보 조회 요청 
        const response = await fetch(`/living/trash/find?tCity=${tCity}&tGu=${tGu}`); // GET => 옵션생략
        const data = await response.json();
        // 2 무엇을 // 응답받은 자료를 특정한 html에 출력한다.
        document.querySelector('#infoBox').innerHTML = data.tinfo;
    }catch(e){
        document.querySelector('#infoBox').innerHTML = `해당 지역은 준비중 입니다.`
    };
}

// [4] 이동함수 : living/trash.jsp 사용자가 접속한 위치의 쿼리스트링이 추가된 living/trash/find?tCity=&tGu= 로 이동

const trashMove = (tCity, tGu) => {
  const urlParams = new URLSearchParams(window.location.search);
  const currenttCity = urlParams.get('tCity');
  const currenttGu = urlParams.get('tGu');

  // 쿼리스트링이 없는 주소면 이동
  if (currenttCity != tCity || currenttGu != tGu) {
    location.href = `/living/trash.jsp?tCity=${tCity}&tGu=${tGu}`;
  } else { // 아니면 이동 x
    console.log('스트링이 존재하여 URL, 이동하지 않습니다.');
  }
};





