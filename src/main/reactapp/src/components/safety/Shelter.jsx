import { useEffect, useRef } from "react";
import { Run, moveToInitialPosition } from "../../assets/script/safety/shelter";
import Header from "../Header";
import Footer from "../Footer";
import my_location from "../../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg"

// 대피소 페이지
export default function Shelter() {
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
                    <div id="mapText">대피소 위치 정보</div>
                    <button id="mapLocation" type="button" onClick={() => moveToInitialPosition(mapRef, pos)}><img src={my_location} /></button>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}