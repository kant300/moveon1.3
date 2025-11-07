import "../../assets/css/member/findId.css"
import { Link, useNavigate } from "react-router-dom";
import Footer from "../Footer";
import Header from "../Header";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import React, {useState} from "react";
import axios from "axios";

export default function FindId() {
    const [ email, setEmail ] = useState( "" );
    const [ phone, setPhone ] = useState( "" );
    const [ foundId, setFoundId ] = useState( "" ); // 검색된 아이디 저장용
    const [ error, setError ] = useState( "" ); // 에러메시지 저장용
    const [ loading, setLoading ] = useState( false ); // 로딩 상태 저장용
    const navigate = useNavigate();


    const handleFindId = async () => {
        setFoundId( "" );
        setError( "" );
        setLoading( true ); //로딩시작

        if( !email || !phone ) {
            setError("이메일과 휴대폰 번호를 모두 입력해주세요.");
            setLoading( false ); // 로딩종료
            return
        }

        try{
            const response = await axios.get("http://localhost:8080/api/member/findid",{
                params: { memail: email, mphone: phone }
            });
            if( response.data && response.data.mid ){
                setFoundId( response.data.mid ); // 성공시 foundId에 아이디 저장
            }else{
                setError("일치하는 회원정보가 없습니다."); // 응답은 받았으나 아이디가 없을때
            }
        }catch( err ){  // 통신 실패등 에러발생시
            setError("일치하는 회원정보가 없습니다.");
        }finally{
            setLoading( false );
    }
}
    return (<>
        <Header />
        <div id="wrap">
            <div id="container">
                <div id="content_gray">
                    <div id="contentTop">
                        <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor: "pointer"}} />
                        <div id='title'>아이디/비밀번호 찾기</div>
                        <div>　</div>
                    </div>
                    <div id="linkBtn">
                        <Link to='/findId'><div id="selected">아이디 찾기</div></Link>
                        <Link to='/findPwd'><div>비밀번호 찾기</div></Link>
                    </div>
                    <div id="mainMenu">
                        <div className="inputDiv">이메일
                            <input
                                type="text"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="이메일 주소를 입력해주세요"
                            />
                        </div>
                        <div className="inputDiv">휴대폰
                            <input type="text"
                                value={phone}
                                onChange={(e) => setPhone(e.target.value)}
                                placeholder="010-0000-0000 형식으로"
                            />
                        </div>
                    </div>
                    <button
                        type="button"
                        className="button"
                        onClick={handleFindId}
                        disabled={loading}
                    >
                        { loading ? "조회 중..." : "확인" }
                    </button>
                    { foundId && (
                        <div className="result-success"> 고객님의 아이디는 <br />
                            <strong>{foundId}</strong><br />
                            입니다.
                        </div>
                    )}
                    { error && !foundId &&(
                        <div className="result-error">{error}</div>
                    )}

                </div>
            </div>
        </div>
        <Footer />

    </>)
}