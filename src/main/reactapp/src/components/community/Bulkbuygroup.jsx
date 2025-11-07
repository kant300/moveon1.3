import axios from "axios";
import { useEffect, useState } from "react";
import Header from "../Header";
import Footer from "../Footer";
import "../../assets/css/community/bulkbuygroup.css";
import { useNavigate } from "react-router-dom";

export default function Bulkbuygroup() {
  const [groups, setGroups] = useState([]);
  const [keyword, setkeyword] = useState("");
  const [auth, setAuth] = useState({ check: null })

  //  글 목록 불러오기
  const fetchGroups = async () => {
    try {
      const response = await axios.get("http://localhost:8080/group/list");
      setGroups(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("❌ 소분모임 목록 조회 실패:", error);
    }
  };
  // 검색 키워드
  const keybod = async (e) => {
    const value = e.target.value;
    setkeyword(value);
    try {
      if (value.trim() == "") {
        fetchGroups();
      } else {
        const response = await axios.get("http://localhost:8080/group/listprint", {
          params: { btitle: value, bcontent: value },
        });
        setGroups(response.data);
      }
    } catch (e) { console.log(e) }

  }

  // 최촛 ㅣㄹ행 렌더링1번
  useEffect(() => {
    checkcookie();
    fetchGroups();

  }, []);

  const navigate = useNavigate();
  // 로그인 정보 가져오기
  const checkcookie = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/member/info",
        { withCredentials: true });
      setAuth(res.data);
      console.log(res.data)
      if (res.data === null) {
        alert('로그인후 이용해주세요');
        navigate('/login');
      }
    } catch (e) { setAuth({ check: false }) };

  }


  //  글쓰기 버튼 클릭
  const handleWriteClick = () => {
    if(!auth.mno){
      alert('로그인 후 이용해주세요.')
    }
    else{
    navigate("/group/create");
    }
  };


  // 방 입장 시 인원 +1
const 입장 = async (item) => {
  if(!auth.mno){
    alert('로그인 후 이용해주세요. ');
    navigate('/login');
    return;
  }

  // 이미 참여중인 방인지 확인
  try{
    const check = await axios.get("http://localhost:8080/groupchat/my/Group" , {
      params : { mno : auth.mno },
      withCredentials : true,
    });

    // bno 방 참여중인지 검사
    const joincount =check.data.some((g) => g.bno === item.bno);
    if(joincount) {
      alert(" 이미 참여중인 방입니다. 채팅방으로 이동합니다. ");
      navigate(`/community/chatting/${item.bno}`);
      return; // 중복 방지
    }
  }catch(error) {console.log("참여 여부 오류 발생"+error);

  }

  if (item.bcount < item.btotal) { // 인원 제한 조건 수정
    try {
      // 방입장시 마이페이지 저장 
      await axios.post("http://localhost:8080/groupchat/join/Group" , null, {
        params: { mno: auth.mno, bno: item.bno },
        withCredentials: true,
      })
      const response = await axios.put(
        "http://localhost:8080/chat/count/pp",
        null,
        {
          params: { bno: item.bno },
          withCredentials: true,
        }
      );

      if (response.status === 200) {
        await fetchGroups();
        navigate(`/community/chatting/${item.bno}`, {
          state: { btotal: item.btotal, bcount: item.bcount + 1 },
        });
      } 
    } catch (e) {
      console.error(" 입장 오류:", e);
        alert(" 인원 가득참 또는 실패");
        window.location.reload(); // 화면 자동 새로고침 
      
    }
  } else {
    alert(" 인원 가득참! 더 이상 입장할 수 없습니다.");
  }
};





  return (
    <>
      <Header />
      <div id="wrap">
      <div id="main" className="container">
        <div className="bulk-header">
          <div className="search-bar">
            <input type="text" value={keyword} onChange={keybod} placeholder="제목 또는 내용 검색..." className="search-input" />
          </div>
            <button onClick={handleWriteClick} className="write-btn">
              +소분등록
            </button>
        </div>

        {/*  소분모임 카드 목록 */}
        <div className="bulk-list">
          {groups.length === 0 ? (
            <p style={{ textAlign: "center", marginTop: "30px" }}>
              등록된 소분모임이 없습니다.
            </p>
          ) : (
            groups.map((item) => (
              <div key={item.bno} className="bulk-card" onClick={() => { 입장(item) }}>
              <div className="img">
                <img
                  src={`http://localhost:5173/upload/product/${item.bimg == null ? '이미지준비중.png' : item.bimg }`}
                />
              </div>
                <div className="itemInfo">
                  <h5>{item.btitle}</h5>
                  <p className="content">{item.bcontent}</p>
                  <div className="info"> 인원 : <span className="count">{item.bcount}/{item.btotal}</span></div>
                  <div className="region">{item.maddress1} {item.maddress2} {item.maddress3}</div>
                </div>

              </div>
            ))
          )}
        </div>
      </div>
      </div>
      <Footer />
    </>
  );
}
