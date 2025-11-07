import "../../assets/css/transport/station.css"
import { Run, moveToInitialPosition } from "../../assets/script/transport/station"
import { useEffect, useRef } from "react";
import Footer from "../Footer";
import Header from "../Header";
import my_location from "../../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"

// 지하철 페이지
export default function Station() {
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
                    <div id="mapText">지하철 승강기 위치 및 배차 정보</div>
                    <button id="mapLocation" type="button" onClick={() => moveToInitialPosition(mapRef, pos)}><img src={my_location}/></button>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}