import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Header from "../Header";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import home from '../../assets/images/icons/home_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import axios from "axios";
import Footer from "../Footer";

export default function Signout(){
    const [ agree, setAgree ] = useState("");
    const navigate = useNavigate();

    {/* 회원탈퇴 요청 */}
    const handleSignout = async() => {
        if(agree !== "동의함"){
            alert("'동의함'을 입력해야 탈퇴가 가능합니다.");
            return;
        }
        if (window.confirm("정말로 탈퇴하시겠습니까?" )) {
            try{
                const res = await axios.delete("http://localhost:8080/api/member/signout", {
                    withCredentials:true,
                });
                alert("탈퇴가 완료되었습니다.");
                //navigate("/");
                location.href="/"
            }catch(err){
                alert("탈퇴 중 오류가 발생했습니다. 다시 시도해주세요.");
                console.error(err);
            }
    }
}; 
   
    return(<>
    <Header /> 
        <div id="wrap">
            <div id="container">
                <div id="content_gray">
                <div id="contentTop">
                    <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor: "pointer"}} />
                    <div id='title'>회원탈퇴</div>
                    <div>　</div>
                </div>
                <div className="signout_content">
                    <h3> 회원 탈퇴 안내</h3>
                    <ul>
                        <li> 탈퇴 시 회원님과 관련된 항목은 모두 삭제되어 복구가 불가능합니다.</li>
                        <li> 탈퇴 시 보유하신 포인트는 모두 소멸됩니다.</li>
                        <li> 개인정보보호지침에 의거하여 해당되는 경우는 개인정보를 일부 보관합니다.</li>
                        <li> 탈퇴 후 재가입은 고객센터로 문의해 주세요.</li>
                        <li> 탈퇴한 계정(ID)은 5년간 재사용이 불가합니다.</li>
                    </ul>
                    <h3>회원 탈퇴 동의</h3>
                    <p>위 안내사항을 모두 확인하였으며, 아래 “동의함”을 입력해주세요.</p>
                    <input type="text" placeholder="동의함" value={agree} onChange={(e) => setAgree(e.target.value)} />
                    <div className="signout-buttons">
                        <button className="cancel-btn" onClick={() => navigate(-1)}>
                        취소
                        </button>
                        <button className="confirm-btn" onClick={handleSignout}>
                        확인
                        </button>
                    </div>
                </div>
                </div>
        
            </div>
        </div>
        <Footer />
    </> );

}