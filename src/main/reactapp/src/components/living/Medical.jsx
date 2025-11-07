import { Run, moveToInitialPosition } from "../../assets/script/living/medical"
import { useEffect, useRef } from "react";
import Footer from "../Footer";
import Header from "../Header";
import my_location from "../../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"

// 약국/병원 페이지
export default function Parking() {
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
                    <div id="mapText">약국/병원 위치 정보</div>
                    <button id="mapLocation" type="button" onClick={() => moveToInitialPosition(mapRef, pos)}><img src={my_location}/></button>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}