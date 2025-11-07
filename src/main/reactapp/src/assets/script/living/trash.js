import axios from 'axios'
import waste_normal from "../../images/icons/free-icon-garbage-8134426.png"
import waste_food from "../../images/icons/free-icon-waste-8134488.png"
import waste_large from "../../images/icons/free-icon-waste-8134528.png"
import waste_recycle from "../../images/icons/free-icon-waste-8134678.png"


// [1] 카카오맵 API을 이용하여 현재 위치좌표 => 현재 위치 지역 시,구 정보얻기 
export function Run() {
    const script = document.createElement("script")
    script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&libraries=services&autoload=false";
    script.async = true;
    script.onload = () => {
        kakao.maps.load(() => {
            // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
            if (navigator.geolocation) {
                // GeoLocation을 이용해서 접속 위치를 얻어옵니다
                navigator.geolocation.getCurrentPosition(function (position) {
                    var lat = position.coords.latitude, // 위도
                        lon = position.coords.longitude; // 경도
                    geocoder.coord2RegionCode(lon, lat, callback);
                });
            }

            // [2] 좌표를 주소로 변환, 화면에 출력
            var geocoder = new kakao.maps.services.Geocoder();
            var callback = function (result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    // region_1depth_name : 시도 단위 , region_2depth_name : 구 단위
                    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-to-district // 추후 커스텀시 지역정보코드모음

                    // textBox(jsp)에 현재 접속한 (시도단위, 구단위)위치 출력 
                    const textBox = document.querySelector('#textBox');
                    textBox.innerHTML = `<strong>${result[0].region_1depth_name} ${result[0].region_2depth_name}</strong>의 쓰레기 배출 정보`;
                    document.querySelector('#infoBox').innerHTML = "불러오는 중...";

                    const tCity = result[0].region_1depth_name;
                    const tGu = result[0].region_2depth_name;

                    trashFind(tCity, tGu);
                }
            };
        })
    }
    document.head.appendChild(script);
}

// [3] 쓰레기 개별 배출정보 출력
async function trashFind(tCity, tGu) {
    try {
        const response = await axios.get(`http://localhost:8080/living/trash?tCity=${tCity}&tGu=${tGu}`);
        const data = await response.data;
        
        const html = `
                        <div class='trashText'>
                            <div>
                                <div>미수거일: ${data.미수거일}</div>
                                <div>배출장소: ${data.배출장소}</div>
                            </div>
                        </div>
                        <div class='trashInfoDiv'>
                            <div>
                                <img src=${waste_normal} />
                            </div>
                            <div>
                                <div class='trashTitle'>생활쓰레기</div>
                                <div>배출방법: ${data.생활쓰레기배출방법}</div>
                                <div>배출시각: ${data.생활쓰레기배출시작시각} ~ ${data.생활쓰레기배출종료시각}</div>
                                <div>배출요일: ${data.생활쓰레기배출요일}</div>
                            </div>
                        </div>
                        <div class='trashInfoDiv'>
                            <div>
                                <img src=${waste_food} />
                            </div>
                            <div>
                                <div class='trashTitle'>음식물쓰레기</div>
                                <div>배출방법: ${data.음식물쓰레기배출방법}</div>
                                <div>배출시각: ${data.음식물쓰레기배출시작시각} ~ ${data.음식물쓰레기배출종료시각}</div>
                                <div>배출요일: ${data.음식물쓰레기배출요일}</div>
                            </div>
                        </div>
                        <div class='trashInfoDiv'>
                            <div>
                                <img src=${waste_large} />
                            </div>
                            <div>
                                <div class='trashTitle'>일시적다량폐기물</div>
                                <div>배출방법: ${data.일시적다량폐기물배출방법}</div>
                                <div>배출시각: ${data.일시적다량폐기물배출시작시각} ~ ${data.일시적다량폐기물배출종료시각}</div>
                                <div>배출장소: ${data.일시적다량폐기물배출장소}</div>
                            </div>
                        </div>
                        <div class='trashInfoDiv'>
                            <div>
                                <img src=${waste_recycle} />
                            </div>
                            <div>
                                <div class='trashTitle'>재활용품</div>
                                <div>배출방법: ${data.재활용품배출방법}</div>
                                <div>배출시각: ${data.재활용품배출시작시각} ~ ${data.재활용품배출종료시각}</div>
                                <div>배출요일: ${data.재활용품배출요일}</div>
                            </div>
                        </div>
                        <div class='trashText'>
                            <div>
                                <div>관리부서명: ${data.관리부서명}</div>
                                <div>관리부서전화번호: ${data.관리부서전화번호}</div>
                            </div>
                        </div>
        `

        document.querySelector('#infoBox').innerHTML = html;
    } catch (e) {
        document.querySelector('#infoBox').innerHTML = `해당 지역은 준비중 입니다.`
    };
}