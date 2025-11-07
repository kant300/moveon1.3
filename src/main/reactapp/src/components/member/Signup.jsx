import '../../assets/css/member/signup.css'
import Footer from "../Footer";
import Header from "../Header";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';
import { useState } from 'react';

// 회원가입 페이지
export default function Signup() {

    const navigate = useNavigate();

    // ------------------------------ 주소 데이터 샘플 ------------------------------
    const cityData = ["서울특별시", "인천광역시", "경기도"];

    const districtData = {
        "서울특별시": ["강남구", "서초구", "송파구", "마포구"],
        "인천광역시": ["남동구", "연수구", "부평구", "계양구", "미추홀구", "서구"],
        "경기도": ["수원시", "성남시", "용인시", "고양시"]
    };

    const townData = {
        "남동구": ["구월동", "논현동", "만수동"],
        "연수구": ["송도동", "연수동", "옥련동"],
        "부평구": ["부평동", "청천동", "삼산동"],
        "계양구": ["계산동", "작전동"],
        "미추홀구": ["주안동", "도화동"],
        "서구": ["검암동", "청라동", "가정동"],
        "강남구": ["삼성동", "역삼동", "청담동"],
        "서초구": ["서초동", "방배동"],
        "송파구": ["잠실동", "가락동"],
        "마포구": ["합정동", "서교동"],
        "수원시": ["영통구", "권선구", "팔달구"],
        "성남시": ["분당구", "수정구"],
        "용인시": ["기흥구", "수지구"],
        "고양시": ["일산동구", "덕양구"]
    };

    // ------------------------------ 상태 관리 ------------------------------
    const [selectedCity, setSelectedCity] = useState("");
    const [selectedDistrict, setSelectedDistrict] = useState("");
    const [districtList, setDistrictList] = useState([]);
    const [townList, setTownList] = useState([]);

    // 시/도 변경 시
    const handleCityChange = (e) => {
        const city = e.target.value;
        setSelectedCity(city);
        setSelectedDistrict("");
        setTownList([]);
        setDistrictList(districtData[city] || []);
    };

    // 구/군 변경 시
    const handleDistrictChange = (e) => {
        const district = e.target.value;
        setSelectedDistrict(district);
        setTownList(townData[district] || []);
    };

    // ------------------------------ 회원가입 요청 ------------------------------
    const signup = async () => {
        const mid = document.querySelector("#idInput").value;
        const mpwd = document.querySelector("#pwdInput").value;
        const pwdCheck = document.querySelector("#pwdCheckInput").value;
        const mname = document.querySelector("#nameInput").value;
        const memail = document.querySelector("#emailInput").value;
        const mphone = document.querySelector("#phoneInput").value;

        const maddress1 = selectedCity;
        const maddress2 = selectedDistrict;
        const maddress3 = document.querySelector("#addr3Input").value;

        if (mpwd !== pwdCheck) {
            alert("비밀번호를 정확히 입력해주세요.");
            return;
        }

        try {
            const obj = { mid, mpwd, mname, memail, mphone, maddress1, maddress2, maddress3 };
            const response = await axios.post("http://localhost:8080/api/member/signup", obj);
            const data = response.data;

            if (data === true ) {
                alert("회원가입 성공");
                navigate("/login");
            } else {
                alert("회원가입 실패");
            }
        } catch (error) {
            if (error.response) {
                if (error.response.status === 500) {
                    alert("회원가입 실패 (중복되는 값이 있습니다.)");
                } else {
                    alert(`회원가입 실패 (오류코드: ${error.response.status})`);
                }
            }
        }
    };

    // ------------------------------ JSX ------------------------------
    return (
        <>
            <Header />
            <div id="wrap">
                <div id="container">
                    <div id="content_gray">
                        <div id="contentTop">
                            <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor: "pointer"}} alt="back" />
                            <div id='title'>회원가입</div>
                            <div>　</div>
                        </div>

                        <div>
                            <div id='signupDiv'>
                                <div><div>아이디</div> <input type="text" id="idInput" className="input" placeholder="아이디를 입력해주세요" /></div>
                                <div>비밀번호 <input type="password" id="pwdInput" className="input" placeholder="비밀번호를 입력해주세요" /></div>
                                <div>비밀번호 확인 <input type="password" id="pwdCheckInput" className="input" placeholder="비밀번호를 다시 입력해주세요" /></div>
                                <div>닉네임 <input type="text" id="nameInput" className="input" placeholder="닉네임을 입력해주세요" /></div>
                                <div>이메일 <input type="text" id="emailInput" className="input" placeholder="이메일 주소를 입력해주세요" /></div>
                                <div>전화번호 <input type="text" id="phoneInput" className="input" placeholder="전화번호를 입력해주세요" /></div>

                                <div id='addressTitle'>주소</div>
                                <div id='address'>
                                    <select id='addr1Input' value={selectedCity} onChange={handleCityChange}>
                                        <option value="">시/도</option>
                                        {cityData.map((city) => (
                                            <option key={city} value={city}>{city}</option>
                                        ))}
                                    </select>

                                    <select id='addr2Input' value={selectedDistrict} onChange={handleDistrictChange}>
                                        <option value="">구/군</option>
                                        {districtList.map((district) => (
                                            <option key={district} value={district}>{district}</option>
                                        ))}
                                    </select>

                                    <select id='addr3Input'>
                                        <option value="">동/읍/면</option>
                                        {townList.map((town) => (
                                            <option key={town} value={town}>{town}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div id='btnDiv'>
                            <button type='button' className='button' onClick={signup}>회원가입</button>
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}
