import '../assets/css/notfound.css'
import { Link, useNavigate } from "react-router-dom";
import Footer from "./Footer";
import Header from "./Header";

// 존재하지 않는 페이지
export default function NotFound() {
    const navigate = useNavigate();

    return (<>
        <Header />
        <div id="wrap">
            <div id="container" style={{width: '420px', height: '86vh'}}>
                <div>존재하지 않는 페이지입니다.</div>
                <div id='btnDiv'>
                    <button onClick={() => navigate(-1)} style={{cursor: "pointer", width: "200px"}}>이전 페이지로 가기</button>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}