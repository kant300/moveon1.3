import { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import arrow_forward_ios from '../../assets/images/icons/arrow_forward_ios_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import home from '../../assets/images/icons/home_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import Header from "../Header";
import Footer from "../Footer";
import '../../assets/css/member/MyInfo.css';

export default function MyInfo() {
    const [ member, setMember ] = useState(null); // 로그인된  멤버 정보저장
    const navigate = useNavigate();

    {/* 회원정보 불러오기 */}
    const getSignout = async()=>{
        try{
            const url = "http://localhost:8080/api/member/info"
            const res = await axios.get(url,{ withCredentials : true } );
            if(res.data !=''){
                setMember( res.data );
            }
        }catch(err){ setMember(null); }
    }
    useEffect( () => { getSignout(); }, [] );


    {/* 회원데이터가 없으면 로딩 표시 */}
    if(!member) return<div>회원정보를 불러오는 중입니다...</div>;

    return(<>
        {/* 헤더 */ }
        <Header /> 
            <div id="wrap">
                <div id="container">
                    <div id="content_gray">
                        <div id="contentTop">
                        <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor: "pointer"}} />
                        <div id='title'>회원정보관리</div>
                        <div>　</div>
                    </div>

                    {/* 회원 기본 정보 */}
                    <div id="myinfo_basic">
                        <div id="myinfo_id"> {member.mid} </div>
                        <div id="myinfo_date"> 가입일 : {member.mdate}  </div>
                        <div id="status"><span id="status-dot">정상</span></div>
                    </div>

                    {/* 회원 상세 정보 */}
                    <div className="myinfo_detail">
                        <div className="info_item">
                            <div className="info_label_wrap">
                                <span className="info_label">이름</span><br />
                                <span className="info_value">{member.mname}</span>
                            </div> 
                            <Link to="/modifyName">변경하기<img src={arrow_forward_ios} /></Link>
                        </div>
                        <div className="info_item">
                            <div className="info_label_wrap">
                                <span className="info_label">이메일</span><br />
                                <span className="info_value">{member.memail}</span>
                            </div>
                            <Link to="/modifyEmail">변경하기<img src={arrow_forward_ios} /></Link>
                        </div>
                        <div className="info_item">
                            <div className="info_label_wrap">
                                <span className="info_label">휴대폰번호</span><br />
                                <span className="info_value">{member.mphone}</span>
                            </div>
                            <Link to="/modifyPhone">변경하기<img src={arrow_forward_ios} /></Link>
                        </div>
                        <div className="info_item">
                            <div className="info_label_wrap">
                                <span className="info_label">주소변경</span><br />
                                <span className="info_value">{member.maddress1} {member.maddress2} {member.maddress3}</span>
                            </div>
                            <Link to="/modifyAddress">변경하기<img src={arrow_forward_ios} /></Link>
                        </div>
                    </div>
                    <div className="myinfo_detail1">
                        <div className="info_item">
                            <span className="info_pwd" style={{ fontSize: '18px', color : 'black' }}>비밀번호</span>
                            <Link to="/modifyPwd">변경하기<img src={arrow_forward_ios} /></Link>
                        </div>
                    </div>
                    <div className="myinfo_detail2">
                        <div className="info_item">
                            <Link to="/signout"> 회원탈퇴</Link> 
                        </div>
                    </div> 
                    </div>
                    
                    
                </div>
            </div>
       
            <Footer />
        </>)
}