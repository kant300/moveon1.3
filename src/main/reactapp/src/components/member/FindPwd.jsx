import '../../assets/css/member/findPwd.css'
import { Link, useNavigate } from "react-router-dom";
import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import Footer from "../Footer";
import Header from "../Header";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'

export default function FindPwd() {
    const navigate = useNavigate();

    // 1. 입력상태
    const [ mid, setMid ] = useState( "" );
    const [ memail, setMemail ] = useState( "" );
    const [ verifyCode, setVerifyCode ] = useState( "" );

    // 2. 인증/타이머 상태
    const [ authRequested, setAuthRequested ] = useState( false ); // 인증요청여부
    const [ authComplete, setAuthComplete ] = useState( false ); // 인증완료여부
    const [ timer, setTimer ] = useState( 0 ); // 남은 시간(초)
    const [ statusMessage, setStatusMessage ] = useState( "" ); // 회원에게 보여줄 메시지

    const timeRef = useRef( null ); // 타이머 관리를 위한 Ref

    // 3. 타이머 관리 로직( useEffect )
    useEffect( () => {
        // 타이머 시작 조건 : 인증요청 0 , 타이머> 0
        if( authRequested && timer > 0 ){
            timeRef.current = setInterval( () => {
                setTimer( (prevTimer) => prevTimer - 1 );
            }, 1000 );
        }else if( timer === 0  && authRequested){
            // 타이머 만료시
            clearInterval( timeRef.current );
            setSatusMessage( "인증 시간이 만료되었습니다. 다시 요청해주세요." );
            setAuthRequested( false ); // 인증요청 초기화
        }
        return () => clearInterval( timeRef.current );  // 컴포넌트 언마운트 시 클리어
    }, [ authRequested, timer ] );

    //  시간 포맷
    const formatTime = ( seconds ) => {
        const minutes = Math.floor( seconds / 60 );
        const remainingSeconds = seconds % 60;
        return `${minutes.toString().padStart(2,'0')}:${remainingSeconds.toString().padStart(2,'0')}`;
    };
    // 4. [API 1]이메일 인증 요청 및 타이머 시작
    const handleRequestAuth = async() => {
        if( !mid || !memail ){
            setStatusMessage( "아이디와 이메일을 모두 입력해주세요." );
            return;
        }
        setStatusMessage("인증번호를 전송중입니다...");
        setAuthRequested( true );
        setAuthComplete( false );
        setTimer(3 *60); // 3분 타이머 설정
        try{
            // [API 1] Spring Boot 인증메일 요청 API 호출
            const response = await axios.post("http://localhost:8080/api/member/requestPwdAuth",
                { mid, memail});
                if( response.data.success ){
                    setStatusMessage("인증번호가 이메일로 전송되었습니다.");
                }else{
                    setStatusMessage(response.data.message || "인증번호가 일치하지 않습니다.");
                    setAuthRequested( false );
                }
        }catch( err ){
            setStatusMessage("인증번호중 오류가 발생했습니다." );
            setAuthRequested( false );
        }
    };
        // 5. [API 2] 인증번호 확인
        const handleVerifyCode = async() => {
            if( !verifyCode ){
                setStatusMessage( "인증번호를 입력해주세요." );
                return;
            }
            setStatusMessage("인증번호를 확인중입니다...");
            try{
                // [API 2] Spring Boot 인증번호 확인 API 호출
                const response = await axios.post("http://localhost:8080/api/member/verifyPwdCode",
                { mid,  verifyCode });
                if(response.data.success){ // 인증 성공
                    setAuthComplete( true );
                    clearInterval( timeRef.current ); // 타이머 정지
                    setAuthRequested( false );  // 인증버튼 초기화
                    setStatusMessage("인증이 완료되었습니다. 비밀번호를 재설정해주세요.");
                }else{ // 인증 실패
                    setStatusMessage(response.data.message || "인증번호가 일치하지 않습니다.");
                }
            }catch( err ){
                setStatusMessage("인증번호 확인중 오류가 발생했습니다." );
            }
        };
        // 6. 최종확인버튼(비밀번호 재설정 페이지로 이동)
        const handleConfirm = () => {
            if( !authComplete ){
                setStatusMessage("인증을 완료해주세요.");
                return;
            }
            navigate( '/resetPwd' , { state : { mid : mid } } );
        };

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
                        <Link to='/findId'><div>아이디 찾기</div></Link>
                        <Link to='/findPwd'><div id="selected">비밀번호 찾기</div></Link>
                    </div>
                    <div id="mainMenu">
                        <div className="inputDiv">
                            아이디 <input type="text" id="idInput" placeholder="ID를 입력해주세요"
                                value={mid} onChange={(e)=> setMid(e.target.value)} disabled={authComplete}/>
                        </div>
                        <div className="inputDiv">
                            이메일 <input type="text" id="emailInput" placeholder="이메일 주소를 입력해주세요"
                                value={memail} onChange={(e)=> setMemail(e.target.value) } disabled={authComplete}/>
                        </div>
                        {/* 인증/재요청 버튼 */}
                        <div className='inputDiv buttonDiv'>
                            <button
                                type="button"
                                className="button_small"
                                onClick={handleRequestAuth}
                                // 인증완료시 또는 타이머 동작 중에는 비활성화
                                disabled={authComplete || (authRequested && timer > 0 )}
                            >
                                {authRequested && timer > 0 ? `재요청(${formatTime(timer)})` : '인증'}
                            </button>
                        </div>
                        {/* 인증번호 입력 필드는 인증 요청 후에만 표시 */}
                        {authRequested && (
                            <>
                                <div className="inputDiv">
                                    인증번호 <input type="text" id="verifyInput" placeholder="인증 번호를 입력해주세요"
                                    value={verifyCode} onChange={(e)=>setVerifyCode(e.target.value)} disabled={authComplete} />
                                </div>
                                <div className='inputDiv buttonDiv'>
                                    <button type="button"
                                    className="button_small"
                                    onClick={handleVerifyCode}
                                    disabled={authComplete}
                                    >확인
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
            {/* 상태 메시지 출력(화면 하단) */}
            { statusMessage && (
                <div> {statusMessage} </div>
            )}
            {/* 최종 확인 버튼 : 인증 완료 시에만 활성화 */}
            <button type="button" className="button" onClick={handleConfirm} disabled={!authComplete}>
                확인
            </button>
        </div>
    </div>
</div>
        <Footer />
    </>);
}