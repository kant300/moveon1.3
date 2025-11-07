import { Link, useNavigate } from 'react-router-dom';
import '../assets/css/setting.css'
import arrow_back_ios_new from '../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import arrow_forward_ios from '../assets/images/icons/arrow_forward_ios_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import toggle_on from '../assets/images/icons/toggle_on_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import toggle_off from '../assets/images/icons/toggle_off_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import Footer from "./Footer";
import Header from './Header';
import { useEffect, useState } from 'react';
import axios from 'axios';


// 설정 페이지
export default function Setting() {
    const navigate = useNavigate();

    // 토글 상태 관리
    const [ isAutoLogin, setIsAutoLogin ] = useState(true);
    const [ isMarketingConsent, setIsMarketingConsent ] = useState(true);

        // 1. 로그인된 유저 정보 저장
    const [ member , setMember ] = useState( null );
    // 2. 최초로 컴포넌트 실행시 유저 정보 요청하기
    const getMyInfo = async()=>{
        try{
            const url = "http://localhost:8080/api/member/info"
            const res = await axios.get( url , { withCredentials : true } );
            if (res.data != '') {
                setMember( res.data ); // 반환된 유저 정보를 저장
            }
        }catch( err ){ setMember(null); } // 오류시 null
    }
    useEffect( () => { getMyInfo(); } , [] );

        // 3. 로그아웃 요청하기 
    const getLogout = async()=>{
        try{
            const url = 'http://localhost:8080/api/member/logout'
            const res = await axios.get( url , { withCredentials : true } );
            alert('로그아웃 되었습니다.');
            // navigate("/login"); // 라우터( 클라이언트 사이드 렌더링 )
            location.href="/login" // ( 서버 사이드 렌더링 )
        }catch( err ){ }
    }
    // 4. 자동 로그인 토글 핸들러
    const handleAutoLoginToggle = () => {
            setIsAutoLogin(prev => !prev);
            // 서버 연동 로직 (선택 사항)
        };
    // 5. 마케팅 수신 동의 토글 핸들러
    const handleMarketingConsentToggle = () => {
            setIsMarketingConsent(prev => !prev);
            // 서버 연동 로직 (선택 사항)
        };


    return (<>
        <Header />
        <div id="wrap">
            <div id="container">
                <div id='content_gray'>
                    <div id="contentTop">
                        <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor: "pointer"}} />
                        <div id='title'>설정</div>
                        <div>　</div>
                    </div>
                    <div id="mainSetting">
                        <div className='settingBox'>
                            <h3>개인정보</h3>
                            <div>
                                {
                                    member == null ?
                                    <>
                                        <div>로그인해주세요</div>
                                        <div><Link to='/login'>로그인</Link></div>
                                    </>:
                                    <>
                                        <div> { member == null ?<> null </>:<> {member.mname}님 </>}</div>
                                        <div> <a href="#" onClick={ getLogout } >로그아웃</a></div>
                                    </>
                                }
                            </div>
                            <div>
                                <div>회원정보 관리</div>
                                <div><Link to='/myinfo'><img src={arrow_forward_ios} /></Link></div>
                            </div>
                            <div>
                                <div>자동 로그인</div>
                                <div><img src={ isAutoLogin ? toggle_on : toggle_off } onClick={handleAutoLoginToggle} className='toggleBtn' /></div>
                            </div>
                        </div>
                        <div className='settingBox'>
                            <h3>서비스 이용 설정</h3>
                            <div>
                                <div>마케팅 수신 동의</div>
                                <div><img src={ isMarketingConsent ? toggle_on : toggle_off } onClick={handleMarketingConsentToggle} className='toggleBtn' /></div>
                            </div>
                        </div>
                        <div className='settingBox'>
                            <h3>버전 정보</h3>
                            <div>현재 버전 V 1.1</div>
                            <div>최신 버전 V 1.1</div>
                        </div>
                    </div>
                </div>    
            </div>
        </div>
        <Footer />
    </>)
}