import React, {useState} from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios  from "axios";
import Header from "../Header";
import Footer from "../Footer";

export default function ResetPwd(){
    const navigate = useNavigate();
    const location = useLocation();

    // FindPwd.jsx에서전달받ㅇ느 아이디
    const mid = location.state?.mid;

    const [newPwd, setNewPwd] = useState('');
    const [confirmPwd, setConfirmPwd] = useState('');
    const [statusMessage, setStatusMessage] = useState('');
    const [loading, setLoading] = useState(false);
    // 비밀번호 재설정 처리
    const handleResetPassword = async () => {
        if (!mid) {
            setStatusMessage('잘못된 접근입니다. 비밀번호 찾기 페이지로 돌아가세요.');
            return;
        }
        if (!newPwd || !confirmPwd) {
            setStatusMessage('새 비밀번호와 확인을 모두 입력해주세요.');
            return;
        }
        if (newPwd !== confirmPwd) {
            setStatusMessage('새 비밀번호와 확인이 일치하지 않습니다.');
            return;
        }
        // * 추가: 비밀번호 유효성 검사 로직 추가 필요

        setStatusMessage('비밀번호를 재설정 중입니다...');
        setLoading(true);

        try {
            // [API 3] Spring Controller의 @PutMapping("/findpwd") 호출
            const response = await axios.put("http://localhost:8080/api/member/findpwd", { mid, mpwd: newPwd }); 
            
            if (response.data === true) { // 가정: 서버에서 true 반환 시 성공
                setStatusMessage('비밀번호 재설정이 완료되었습니다. 로그인 페이지로 이동합니다.');
                // 3초 후 로그인 페이지로 이동
                setTimeout(() => {
                    navigate('/login');
                }, 3000);
            } else {
                setStatusMessage('비밀번호 재설정에 실패했습니다. 다시 시도해주세요.');
            }
        } catch (error) {
            setStatusMessage('서버 오류로 인해 재설정에 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };
    return(<>
        <Header />
            <div id="wrap">
                <div id="container">
                    <div id="content_gray">
                        <div id='title' >비밀번호 재설정</div>
                        
                        {/* 비밀번호 재설정 폼 */}
                        <div id="mainMenu">
                            {/* 아이디 정보가 유효할 때만 폼 표시 */}
                            {mid ? (
                                <>
                                    <div className="inputDiv">
                                        새 비밀번호 <input type="password" placeholder="새 비밀번호를 입력해주세요"
                                            value={newPwd} onChange={(e) => setNewPwd(e.target.value)} />
                                    </div>
                                    <div className="inputDiv">
                                        비밀번호 확인 <input type="password" placeholder="새 비밀번호를 다시 입력해주세요"
                                            value={confirmPwd} onChange={(e) => setConfirmPwd(e.target.value)} />
                                    </div>
                                </>
                            ) : (
                                <div>인증 정보가 유효하지 않습니다.</div>
                            )}
                        </div>
                        {/* 상태 메시지 및 버튼 */}
                        {statusMessage && (
                            <div>
                                {statusMessage}
                            </div>
                        )}

                        <button type="button" className="button" onClick={handleResetPassword} disabled={loading || !mid}>
                            {loading ? "처리 중..." : "확인"}
                        </button>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
   
}