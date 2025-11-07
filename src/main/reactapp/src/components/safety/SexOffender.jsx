import "../../assets/css/safety/sexOffender.css"
import Footer from "../Footer";
import Header from "../Header";
import my_location from "../../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"
import { useEffect, useRef } from "react";
import { Run, moveToInitialPosition } from "../../assets/script/safety/sexOffender";

// 성범죄자 위치 페이지
export default function SexOffender() {
    const mapRef = useRef(null);
    const pos = useRef(null);

    useEffect(() => {
        Run(mapRef, pos);
    }, []);

    return (<>
        <Header />
        <div id="wrap">
            <div id="container">
                <div id="map">
                    <div id="mapText"> 성범죄자 위치 정보 
                    <span className="box_search">
                        <input type="search" id="innerQuery" className="tf_keyword" placeholder="장소, 주소검색" maxLength="100" />
                        <button type="button" className="search_button" style={{background_color: "#99E2FF", padding: "0px"}}> 검색 </button>
                    </span>
                    <div id="criminal_container">
                        내 위치 반경 2km 내 등록된 성범죄자 인원수 : <span id="criminal_count"> 0 </span> 명
                    </div>
                </div> 
                <button id="mapLocation" type="button" onClick={() => moveToInitialPosition(mapRef, pos)}><img src={my_location} /></button>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}