import '../../assets/css/living/trash.css'
import { useEffect } from "react"
import Footer from "../Footer.jsx"
import Header from "../Header.jsx"
import { Run } from "../../assets/script/living/trash.js";

// 쓰레기 배출정보 페이지
export default function Trash() {
    useEffect(() => {
        Run();
    }, []);

    return (<>
        <Header />
        <div id="wrap">
            <div id="container">
                <div id="textBox">위치 액세스가 거부되었습니다. 액세스를 허용해주세요.</div>
                <div id="infoBox">
                    {/* 쓰레기 정보 */}
                </div>
            </div>
        </div>
        <Footer />
    </>)
}