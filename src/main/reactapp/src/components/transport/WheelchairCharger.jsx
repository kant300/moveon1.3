import "../../assets/css/transport/station.css"
import { useEffect, useRef } from "react";
import Header from "../Header";
import Footer from "../Footer";
import my_location from "../../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg";
import markerIcon from "../../assets/images/icons/marker.png";

export default function WheelchairCharger() {
  const mapRef = useRef(null);
  const pos = useRef(null);

  useEffect(() => {
    const script = document.createElement("script");
    script.src =
      "//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&autoload=false&libraries=services,clusterer";
    script.async = true;
    script.onload = () => {
      window.kakao.maps.load(() => {
        const map = new kakao.maps.Map(document.getElementById("map"), {
          center: new kakao.maps.LatLng(37.45, 126.70),
          level: 8,
        });

        mapRef.current = map;

        // 현재 위치 가져오기
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition((position) => {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;
            const myLocation = new kakao.maps.LatLng(lat, lon);
            pos.current = myLocation;

            const marker = new kakao.maps.Marker({
              position: myLocation,
              map: map,
            });

            const infowindow = new kakao.maps.InfoWindow({
              content: `<div style="padding:5px;font-family:'NanumGothic';">현재 위치</div>`,
              removable: true,
            });
            infowindow.open(map, marker);
          });
        }

        // 클러스터러
        const clusterer = new kakao.maps.MarkerClusterer({
          map: map,
          averageCenter: true,
          minLevel: 5,
          disableClickZoom: true,
        });

        // Spring Boot API 호출
        const loadChargerData = async () => {
          try {
            const res = await fetch("http://localhost:8080/api/chargers/all");
            const data = await res.json();
            console.log("전동휠체어 급속충전기 데이터:", data);

            // 마커 생성
            const markers = data.map((item) => {
              if (!item.위도 || !item.경도) return null;

              const imageSrc = markerIcon;
              const imageSize = new kakao.maps.Size(25, 34);
              const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

              const marker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(item.위도, item.경도),
                image: markerImage,
              });

              kakao.maps.event.addListener(marker, "click", () => {
                const content = `
                  <div style="width:220px;padding:8px;text-align:center;font-family:'NanumGothic';">
                    <b>${item.시설명}</b><br/>
                     ${item.소재지도로명주소 || item.소재지지번주소}<br/>
                     평일: ${item.평일운영시작시각}~${item.평일운영종료시각}<br/>
                     ${item.관리기관명}
                  </div>
                `;
                const iw = new kakao.maps.InfoWindow({
                  content: content,
                  removable: true,
                });
                iw.open(map, marker);
              });
              return marker;
            }).filter(Boolean);

            clusterer.addMarkers(markers);
          } catch (error) {
            console.error("데이터 로드 실패:", error);
          }
        };

        loadChargerData();
      });
    };
    document.head.appendChild(script);
  }, []);

  //    현재위치 버튼
  const moveToInitialPosition = () => {
    if (mapRef.current && pos.current) {
      mapRef.current.setCenter(pos.current);
    }
  };

  return (
    <>
      <Header />

      <div id="wrap">
        <div id="container">
          <div id="map">
            <div id="mapText"> 전동휠체어 급속충전기 위치</div>
            <button id="mapLocation" type="button" onClick={moveToInitialPosition}>
              <img src={my_location} alt="현재위치" />
            </button>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}