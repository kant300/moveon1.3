import '../assets/css/menu.css'
import Footer from "./Footer";
import notification_sound from '../assets/images/icons/notification_sound_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import settings from '../assets/images/icons/settings_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import paid from '../assets/images/icons/paid_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import movein from '../assets/images/icons/move_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import apparel from '../assets/images/icons/apparel_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import recycling from '../assets/images/icons/recycling_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import eco from '../assets/images/icons/eco_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import office from '../assets/images/icons/things_to_do_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import hospital from '../assets/images/icons/local_hospital_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import crisis_alert from '../assets/images/icons/crisis_alert_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import ambulance from '../assets/images/icons/ambulance_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import water_drop from '../assets/images/icons/water_drop_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import night_shelter from '../assets/images/icons/night_shelter_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import wc from '../assets/images/icons/wc_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import subway from '../assets/images/icons/subway_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import directions_bus from '../assets/images/icons/directions_bus_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import ev_station from '../assets/images/icons/ev_station_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import local_parking from '../assets/images/icons/local_parking_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import gas from '../assets/images/icons/local_gas_station_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import handshake from '../assets/images/icons/handshake_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import explore_nearby from '../assets/images/icons/explore_nearby_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import storefront from '../assets/images/icons/storefront_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import review from '../assets/images/icons/local_activity_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import job from '../assets/images/icons/business_center_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import { Link } from 'react-router-dom';
import Header from './Header';
import { useEffect, useState } from 'react';
import axios from 'axios';

// 전체메뉴 페이지
export default function Menu() {

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

    return (<>
        <Header />
        <div id="wrap">
            <div id='container'>
                <div id="content_gray">
                    <div id="menuTop">
                        <div>{ member == null ? <> 로그인해주세요 </> : <>  {member.mname}</>}</div>
                        <div>
                            <Link to='/setting'><img src={settings} /></Link>
                        </div>
                    </div>
                    <div id='mainMenu'>
                        <div className='menuTitle'><span>●</span>생활</div>
                        <ul id='livingMenu'>
                            <Link to='/living/bill'><li><img src={paid} />공과금정산</li></Link>
                            <a href='https://www.gov.kr/mw/AA020InfoCappView.do?CappBizCD=13100000016'><li><img src={movein} />전입신고</li></a>
                            <Link to='/living/clothingBin'><li><img src={apparel} />의류수거함</li></Link>
                            <Link to='/living/trashInfo'><li><img src={recycling} />쓰레기 배출정보</li></Link>
                            <a href='https://15990903.or.kr/portal/main/main.do'><li><img src={eco} />폐가전수거</li></a>
                            <Link to='/living/government'><li><img src={office} />관공서</li></Link>
                            <Link to='/living/night'><li><img src={hospital} />심야약국/병원</li></Link>
                        </ul>
                        <div className='menuTitle'><span>●</span>안전</div>
                        <ul id='safetyMenu'>
                            <Link to='/safety/sexOffender'><li><img src={crisis_alert} />성범죄자 위치</li></Link>
                            <Link to='/safety/ambulance'><li><img src={ambulance} />민간구급차</li></Link>
                            <Link to='/safety/water'><li><img src={water_drop} />비상급수시설</li></Link>
                            <Link to='/safety/shelter'><li><img src={night_shelter} />대피소</li></Link>
                            <Link to='/safety/restroom'><li><img src={wc} />공중화장실</li></Link>
                        </ul>
                        <div className='menuTitle'><span>●</span>교통</div>
                        <ul id='transportMenu'>
                            <Link to='/transport/subway'><li><img src={subway} />지하철</li></Link>
                            <Link to='/transport/busStation'><li><img src={directions_bus} />버스정류장</li></Link>
                            <Link to='/transport/wheelchairCharger'><li><img src={ev_station} />전동휠체어</li></Link>
                            <Link to='/transport/local_parking'><li><img src={local_parking} />공영주차장</li></Link>
                            <Link to='/transport/station'><li><img src={gas} />주유소</li></Link>
                        </ul>
                        <div className='menuTitle'><span>●</span>커뮤니티</div>
                        <ul id='communityMenu'>
                            <Link to='/community/bulkBuy'><li><img src={handshake} />소분모임</li></Link>
                            <Link to='/community/localEvent'><li><img src={explore_nearby} />지역행사</li></Link>
                            <Link to='/community/localStore'><li><img src={storefront} />지역장터</li></Link>
                            <Link to='/community/localActivity'><li><img src={review} />동네후기</li></Link>
                            <Link to='/community/business'><li><img src={job} />구인/구직</li></Link>
                        </ul>
                        <div className='menuTitle'><span>●</span>고객센터</div>
                        <ul id='inquiryMenu'>
                            <Link to='/inquiry/faq'><li>FAQ</li></Link>
                            <Link to='/inquiry/ask'><li>문의하기</li></Link>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}