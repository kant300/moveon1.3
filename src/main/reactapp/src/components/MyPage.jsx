import "../assets/css/mypage.css"
import Footer from "./Footer";
import settings from '../assets/images/icons/settings_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import account_circle from '../assets/images/icons/account_circle_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import paid from '../assets/images/icons/paid_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import handshake from '../assets/images/icons/handshake_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import explore_nearby from '../assets/images/icons/explore_nearby_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import contact_support from '../assets/images/icons/contact_support_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import { Link, useNavigate } from "react-router-dom";
import Header from "./Header";
import { useEffect, useState } from "react";
import axios from "axios";

// 마이페이지
export default function MyPage() {

    const navigate = useNavigate()
    const [auth, setAuth] = useState({ check: null })
    const checkcookie = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/member/info",
                { withCredentials: true });
                console.log(res.data);
            setAuth(res.data);

            if (res.data === null) {
                alert('로그인후 이용해주세요');
                navigate('/login');
            }
        } catch (e) { setAuth({ check: false }) };
    }

            useEffect(() => {
        checkcookie();
    }, [] )



    return (<>
        <Header />
        <div id="wrap">
            <div id="container">
                <div id="content_gray">
                    <div id="menuTop">
                        <div id="mypageTitle">마이페이지</div>
                        <div>
                            <Link to='/setting'><img src={settings} /></Link>
                        </div>
                    </div>
                    <div id="mainMenu">
                        <div id="profileBox">
                            <div><img src={account_circle} /></div>
                            <div id="profileName">{auth.mname}</div>
                        </div>
                        <div className="mypageTitle"><span>●</span>활동내역</div>
                        <div id="activityBox">
                            <ul>
                                <Link to='/bill?id=0'><li><img src={paid} /> 공과금 정산내역</li></Link>
                                <Link to='/community/MypageBulk'><li><img src={handshake} /> 나의 소분모임</li></Link>
                                <Link to='/localEvent?id=0'><li><img src={explore_nearby} /> 찜한 지역행사</li></Link>
                                <Link to='/inquiry?id=0'><li><img src={contact_support} /> 문의내역</li></Link>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}