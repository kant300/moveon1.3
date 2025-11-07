import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import arrow_back_ios_new from '../../assets/images/icons/arrow_back_ios_new_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg';
import Header from "../Header";
import '../../assets/css/member/ModifyPwd.css';
import Footer from "../Footer";

export default function ModifyPwd() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    mpwd: '',
    newPwd: '',
    newPwdConfirm: ''
  });
  const [message, setMessage] = useState('');

  const onChangeHandler = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const onSubmitHandler = async (e) => {
    e.preventDefault();
    if (form.newPwd !== form.newPwdConfirm) {
      setMessage("새 비밀번호가 일치하지 않습니다.");
      return;
    }
    try {
      const res = await axios.put("http://localhost:8080/api/member/updatePwd", form, { withCredentials: true });
      if (res.data === true) {
        alert("비밀번호가 성공적으로 변경되었습니다.");
        navigate("/myinfo");
      } else {
        setMessage("이전 비밀번호가 일치하지 않습니다.");
      }
    } catch (err) {
      console.error(err);
      setMessage("비밀번호 변경 중 오류가 발생했습니다.");
    }
  };

  return (
    <>
      <Header />
      <div id="wrap">
        <div id="container">
          <div id="content_gray">
            <div id="contentTop">
              <img src={arrow_back_ios_new} onClick={() => navigate(-1)} style={{cursor:"pointer"}}/>
              <div id="title">비밀번호 변경</div>
              <div>　</div>
            </div>

            <form id="pwdForm" onSubmit={onSubmitHandler}>
              <div className="inputBox">
                <label>이전 비밀번호</label>
                <input type="password" name="mpwd" value={form.mpwd} onChange={onChangeHandler} placeholder="이전 비밀번호를 입력해주세요." required />
              </div>
              <div className="inputBox">
                <label>새 비밀번호</label>
                <input type="password" name="newPwd" value={form.newPwd} onChange={onChangeHandler} placeholder="새 비밀번호를 입력해주세요." required />
              </div>
              <div className="inputBox">
                <label>새 비밀번호 확인</label>
                <input type="password" name="newPwdConfirm" value={form.newPwdConfirm} onChange={onChangeHandler} placeholder="새 비밀번호를 다시 입력해주세요." required />
              </div>
              {message && <p className="errorMsg">{message}</p>}

              <div className="btnWrap">
                <button type="button" className="btnCancel" onClick={() => navigate(-1)}>취소</button>
                <button type="submit" className="btnChange">변경</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    <Footer />
    </>
      
  );
}
