import "../assets/css/index.css"
import Footer from "./Footer";
import Header from "./Header";
import calendar_clock from '../assets/images/icons/calendar_clock_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import paid from '../assets/images/icons/paid_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import movein from '../assets/images/icons/move_location_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import recycling from '../assets/images/icons/recycling_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import apparel from '../assets/images/icons/apparel_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import eco from '../assets/images/icons/eco_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import office from '../assets/images/icons/things_to_do_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import hospital from '../assets/images/icons/local_hospital_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import crisis_alert from '../assets/images/icons/crisis_alert_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import ambulance from '../assets/images/icons/ambulance_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import water_drop from '../assets/images/icons/water_drop_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import night_shelter from '../assets/images/icons/night_shelter_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import wc from '../assets/images/icons/wc_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import subway from '../assets/images/icons/subway_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import directions_bus from '../assets/images/icons/directions_bus_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import ev_station from '../assets/images/icons/ev_station_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import local_parking from '../assets/images/icons/local_parking_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import gas from '../assets/images/icons/local_gas_station_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import handshake from '../assets/images/icons/handshake_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import explore_nearby from '../assets/images/icons/explore_nearby_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import storefront from '../assets/images/icons/storefront_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import review from '../assets/images/icons/local_activity_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import job from '../assets/images/icons/business_center_24dp_4DCCFF_FILL0_wght400_GRAD0_opsz24.svg'
import { useEffect, useState } from 'react'
import { Link } from "react-router-dom";
import { Weather } from '../assets/script/index';
import axios from "axios";

// 축제 샘플 이미지 import (Vite 기준)
import ex1_1 from "../assets/images/imgs/ex1_1.jpg";
import ex1_2 from "../assets/images/imgs/ex1_2.jpg";
import ex1_3 from "../assets/images/imgs/ex1_3.png";
import ex1_4 from "../assets/images/imgs/ex1_4.jpg";

// 전시 샘플 이미지 import (Vite 기준)
import ex2_1 from "../assets/images/imgs/ex2_1.jpg";
import ex2_2 from "../assets/images/imgs/ex2_2.jpg";
import ex2_3 from "../assets/images/imgs/ex2_3.jpg";
import ex2_4 from "../assets/images/imgs/ex2_4.jpg";

// 메인 페이지
export default function Index() {
    const [category, setCategory] = useState(0);
    const [event, setEvent] = useState(0);

    useEffect(() => {
        Weather();
        getMyInfo();
    }, [])

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

      // 샘플 데이터 (placehold.co 이미지)
  const eventList = {
    0: [
        { title: "2025 인천공항 스카이페스티벌", addr: "인천광역시 중구 공항문화로 127 (운서동)", img: ex1_1 },
        { title: "2025 인천원도사제&낙섬축제", addr: "인천광역시 미추홀구 매소홀로 120 (용현동)", img: ex1_2 },
        { title: "인천개항장 국가유산 야행", addr: "인천광역시 중구 신포로27번길 80 (관동1가) 인천중구청", img: ex1_3 },
        { title: "2025 인천공항 스카이페스티벌", addr: "인천광역시 중구 공항문화로 127 (운서동)", img: ex1_4 },
    ],
    1: [
        { title: '인천시립교향악단 2025 체임버 시리즈Ⅱ "목관앙상블"', addr: "아트센터 인천", img: ex2_1 },
        { title: "아카펠라그룹 다이아 - 미리 메리 크리스마스", addr: "인천문화예술회관(소공연장)", img: ex2_2 },
        { title: "뮤지컬 <오즈의 의류 수거함>", addr: "인천문화예술회관(소공연장)", img: ex2_3 },
        { title: "해설이 있는 스트릿댄스 - 카르페디엠", addr: "인천문화예술회관(소공연장)", img: ex2_4 },
    ],
  };

    return (<>
        <Header />
        <div id='wrap'>
            <div id="container">
                <div id='loginBox'>{ member == null ? <> 로그인 후 이용해주세요 </> : <>  {member.mname} 님 환영합니다.</>}</div>
                <div id="weatherBox">
                    <div id="weather">
                        {/* 샘플 데이터 */}
                            <div class="w_cont">
                                    <div class="w_inner">
                                        <div class="w_addr"> <strong>인천 부평구 부평동</strong> (16시 기준)  </div>
                                        <div class="w_pty"> 30° 맑음 </div>
                                        <div id="infoBox">
                                            <div id="item1">
                                                <span id="label">습도</span>
                                                <span class="reh">86%</span>
                                            </div>
                                            <div id="item2">
                                                <span id="label">하늘</span>
                                                <span class="sky">맑음</span>
                                            </div>
                                            <div id="item3">
                                                <span id="label">풍속</span>
                                                <span class="wsd">5m/s</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <img src="http://localhost:5173/icons/icons8-sun.svg" style={{width:'95px'}} />
                                </div>
                            
                            </div>
                    </div>
                </div>
                <ul id='categoryBox'>
                    <li className={category===0 ? 'active' : ''} onClick={() => setCategory(0)}>생활</li>
                    <li className={category===1 ? 'active' : ''} onClick={() => setCategory(1)}>안전</li>
                    <li className={category===2 ? 'active' : ''} onClick={() => setCategory(2)}>교통</li>
                    <li className={category===3 ? 'active' : ''} onClick={() => setCategory(3)}>커뮤니티</li>
                </ul>
                <ul id='btnBox'>
                    {
                        category==0?(
                            <>
                                <Link to='/living/bill'><li><img src={paid} />공과금정산</li></Link>
                                <a href="https://www.gov.kr/mw/AA020InfoCappView.do?CappBizCD=13100000016"><li><img src={movein} />전입신고</li></a>
                                <Link to='/living/clothingBin'><li><img src={apparel} />의류수거함</li></Link>
                                <Link to='/living/trashInfo'><li><img src={recycling} />쓰레기배출</li></Link>
                                <a href="https://15990903.or.kr/portal/main/main.do"><li><img src={eco} />폐가전수거</li></a>
                                <Link to='/living/government'><li><img src={office} />관공서</li></Link>
                                <Link to='/living/night'><li><img src={hospital} />심야약국/병원</li></Link>
                            </>
                        ):
                        category==1?(
                            <>
                                <Link to='/safety/sexOffender'><li><img src={crisis_alert} />성범죄자</li></Link>
                                <Link to='/safety/ambulance'><li><img src={ambulance} />민간구급차</li></Link>
                                <Link to='/safety/water'><li><img src={water_drop} />비상급수시설</li></Link>
                                <Link to='/safety/shelter'><li><img src={night_shelter} />대피소</li></Link>
                                <Link to='/safety/restroom'><li><img src={wc} />공중화장실</li></Link>
                            </>
                        ):
                        category==2?(
                            <>
                                <Link to='/transport/subway'><li><img src={subway} />지하철</li></Link>
                                <Link to='/transport/busStation'><li><img src={directions_bus} />버스정류장</li></Link>
                                <Link to='/transport/wheelchairCharger'><li><img src={ev_station} />전동휠체어</li></Link>
                                <Link to='/transport/local_parking'><li><img src={local_parking} />공영주차장</li></Link>
                                <Link to='/transport/station'><li><img src={gas} />주유소</li></Link>
                            </>
                        ):
                        category==3?(
                            <>
                                <Link to='/community/bulkBuy'><li><img src={handshake} />소분모임</li></Link>
                                <Link to='/community/localEvent'><li><img src={explore_nearby} />지역행사</li></Link>
                                <Link to='/community/localStore'><li><img src={storefront} />지역장터</li></Link>
                                <Link to='/community/localActivity'><li><img src={review} />동네후기</li></Link>
                                <Link to='/community/business'><li><img src={job} />구인/구직</li></Link>
                            </>
                        ):null
                    }
                </ul>
                <div id='eventBox'>
                    <ul id='eventCategory'>
                        <li className={ 'active' } >축제/행사</li>
                    </ul>
                    <ul id="eventList">
                        {eventList[0].map((item, i) => (
                        <li key={i} className="event-card">
                            <div className="thumb">
                            <img src={item.img} alt={item.title} />
                            </div>
                            <div className="info">
                            <h4>{item.title}</h4>
                            <p className="price">
                                {item.addr && <span className="sale">{item.addr}</span>}
                            </p>
                            </div>
                        </li>
                        ))}
                    </ul>

                    <ul id='eventCategory'>
                        <li className={ 'active' } >전시/문화</li>
                    </ul>
                    <ul id="eventList">
                        {eventList[1].map((item, i) => (
                        <li key={i} className="event-card">
                            <div className="thumb">
                            <img src={item.img} alt={item.title} />
                            </div>
                            <div className="info">
                            <h4>{item.title}</h4>
                            <p className="price">
                                {item.addr && <span className="sale">{item.addr}</span>}
                            </p>
                            </div>
                        </li>
                        ))}
                    </ul>


                </div>
                <div id='alertBox'>
                    <img src={calendar_clock} />
                    <div>오늘의 알림 : 재활용 배출일입니다.</div>
                </div>
            </div>
        </div>
        <Footer />
    </>)
}