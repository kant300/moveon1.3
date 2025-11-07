import axios from "axios";
import Footer from "../Footer";
import Header from "../Header";
import "../../assets/css/community/chatting.css";
import playicon from '../../assets/images/icons/play_icon_profile.png';
import { useNavigate, useParams } from "react-router-dom";
import { use, useEffect, useState } from "react";

/* MUI : npm install @mui/joy @emotion/react @emotion/styled */
import Box from '@mui/joy/Box';
import Drawer from '@mui/joy/Drawer';
import Button from '@mui/joy/Button';


export default function Chatting() {

  /* MUI 오른쪽 슬라이드바 */
  const [open, setOpen] = useState(false);
  const [player, setplayer] = useState([]);

  const toggleDrawer = (inOpen) => async (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    if (inOpen == true) {
      console.log('mui 슬라이드바 열림');
      try {
        const response = await axios.get("http://localhost:8080/chat/play/name", {
          params: { bno: num },
          withCredentials: true,
        });
        setplayer(response.data);
      } catch (error) { console.log('슬라이드 아레 발생' + error); }
    }
    setOpen(inOpen);
  };
  /* ---------------------- */

  const { bno } = useParams();
  const num = bno;
  const nav = useNavigate();
  const [mmessage, setmmessage] = useState("");
  const [chatprint, setchatprint] = useState([]);
  const [auth, setAuth] = useState(null);
  const [socket, setwebsocket] = useState(null);
  const [count, setcount] = useState({ btotal: 0, bcount: 0, host_mno: 0 });
  const [run, setrun] = useState({});
  const [readonly, setreadonly] = useState(false);

  // ✅ 로그인 정보 가져오기
  const checkcookie = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/member/info", {
        withCredentials: true,
      });
      setAuth(res.data);
      if (!res.data) {
        alert("로그인후 이용해주세요");
        nav("/login");
      }
    } catch (e) {
      console.error(e);
      setAuth(null);
    }
  };

  // ✅ 인원수 체크
  const playcount = async () => {
    const res = await axios.get("http://localhost:8080/chat/cocheck", {
      withCredentials: true,
      params: { bno: num },
    });
    setcount(res.data);
    console.log("🔥 count data:", res.data);
  };

  // ✅ 채팅 출력
  const chattingprint = async () => {
    const res = await axios.get("http://localhost:8080/chat/print", {
      withCredentials: true,
      params: { bno: num },
    });
    setchatprint(res.data);
  };


  // ✅ 최초 로그인 검사
  useEffect(() => {
    checkcookie();
  }, []);


  useEffect(() => {
    if (!auth || !auth.mno) return;

    // ✅ playcount() 실행 후 WebSocket 연결
    playcount().then(() => {
      const sc = new WebSocket("ws://localhost:8080/chatting");
      setwebsocket(sc);

      sc.onopen = () => {
        console.log("✅ WebSocket 연결됨");
        sc.send(
          JSON.stringify({
            type: "join",
            bno: num,
            mname: auth.mname,
            mno: auth.mno,
          })
        );
      };

      sc.onmessage = (event) => {
        const smg = JSON.parse(event.data);
        console.log("📩 메세지 수신:", smg);

        if (smg.type === "alarm") {
          setchatprint((prev) => [
            ...prev,
            { mname: "alarm", mmessage: smg.message },
          ]);

          if (smg.message.includes("읽기모드로 변경")) {
            setreadonly(1);
          }
        } else if (smg.type === "msg") {
          setchatprint((prev) => [
            ...prev,
            {
              mname: smg.mname,
              mno: smg.mno, // ✅ 메시지에도 mno 전달
              mmessage: smg.mmessage,
              cdate: new Date().toISOString(),
            },
          ]);
        } else if (smg.type === "count") {
          setcount((prev) => ({
            ...prev,
            bcount: smg.bcount,
            btotal: smg.btotal,
          }));
        }
      };

      sc.onclose = () => console.log("❌ WebSocket 연결 종료");

      chattingprint();
    });

    return () => socket && socket.close();
  }, [auth, num]);

  const roomcheck = async () => {
    try {
      const response = await axios.get("http://localhost:8080/groupchat/room/lock", {
        params: { bno: num },
        withCredentials: true,
      });
      console.log(response.data);
      setreadonly(response.data.read_only == 1);
    } catch (error) { console.log(error + "읽기 모드 오류 발생 ") }
  }

  //  useEffect 추가
  useEffect(() => {
    roomcheck();
  }, [num]);

  //  스크롤 자동 이동
  useEffect(() => {
    const chattingtop = document.querySelector(".chat-messages");
    if (chattingtop) chattingtop.scrollTop = chattingtop.scrollHeight;
  }, [chatprint]);

  // ✅ 메시지 전송
  const textbtn = async () => {
    const response2 = await axios.get("http://localhost:8080/groupchat/room/lock", {
    params: { bno: num },
    withCredentials: true,
  });


  const roomlock = response2.data.read_only === 1;
  setreadonly(roomlock);

  if (roomlock) {
    alert("읽기 모드에서는 메시지를 전송할 수 없습니다.");
    return;
  }

    if (!mmessage.trim()) return;


    const obj = { bno: num, mmessage };
    const response = await axios.post("http://localhost:8080/chat/write", obj, {
      withCredentials: true,
    });

    if (response.data === true) {
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(
          JSON.stringify({
            type: "msg",
            num: num,
            mno: auth.mno,
            mname: auth.mname,
            mmessage: mmessage,
          })
        );
      }
      setmmessage("");
    } else {
      alert("전송 실패");
    }
  };

  // 방장 여부
  const hostmember = async () => {
    try {
      const response = await axios.get("http://localhost:8080/chat/cocheck", {
        params: { bno: num },
        withCredentials: true,
      });
      return response.data.host_mno === auth.mno;
    } catch (error) {
      console.log("방장 여부 에러발생" + error)
      return false;
    }

  }

  //  퇴장
  const 퇴장 = async () => {
    const hostcheck = await hostmember();

    if (hostcheck) {
      const hostexit = window.confirm(
        "방장님이 나가셨습니다.\n" +
        "읽기 모드로 변경됩니다.\n" +
        "채팅방 나가기 클릭시 입장불가합니다."
      );
      if (!hostexit) return;

      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(
          JSON.stringify({
            type: "alarm",
            bno: num,
            message: "방장님이 나가셨습니다.\n" +
              "읽기 모드로 변경됩니다.\n" +
              "채팅방 나가기 클릭시 입장불가합니다.",
          })
        );
      }
      if (hostcheck) {
        await axios.put("http://localhost:8080/groupchat/room/check", null, {
          params: { gmno: auth.mno, bno: num },
          withCredentials: true,
        });


        await axios.put("http://localhost:8080/groupchat/play/gmnoout", null, {
          params: { gmno: auth.mno, bno: num },
          withCredentials: true,
        });
      }
    }

    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(
        JSON.stringify({
          type: "leave",
          bno: num,
          mname: auth.mname,
          mno: auth.mno,
          message: `${auth.mname}님이 나갔습니다.`,
        })
      );
    }

    try {
      await axios.put("http://localhost:8080/groupchat/leave/Group", null, {
        params: { mno: auth.mno, bno: num },
        withCredentials: true,
      });

      const response = await axios.put(
        "http://localhost:8080/chat/count/mm",
        null,
        { params: { bno: num }, withCredentials: true }
      );

      if (response.status === 200) {
        alert(`방 퇴장 성공 (${num})`);
        nav(`/community/bulkBuy`);
      }
    } catch (e) {
      console.error("퇴장 실패:", e);
    }
  };


  console.log(" chatprint:", chatprint);




  return (
    <>
      <Header />
      <div className="wrap">
        <div className="container">
          <div className="chat-header">

            <div className="chat-title-box">
              <span className="chat-title">{count?.btitle || "같이 구매할 분 구해요"}</span>
              <span className="countcheck">{count.bcount} / {count.btotal}</span>
            </div>

            <Button variant="" className="menu-btn" onClick={toggleDrawer(true)}>≡</Button>

            <Drawer anchor="right" open={open} onClose={toggleDrawer(false)}>
              <Box
                className="drawer-box"
                role="presentation"
                onClick={toggleDrawer(false)}
                onKeyDown={toggleDrawer(false)}
              >
                <h3>현재 채팅방 접속 명단</h3>
                <div className="member-list">
                  {/* <img src={p.membersimg || playicon } alt="프로필" className="profile"
                onError={ (e) => {e.target.src = playicon; } }  // 기본적으로 프로필 가져오는데 프로필이 없을 경우 playicon 기본 프로필 사용
                //  back 이미지 없어서 사용xx
                /> */}
                  {player.length > 0 ? (
                    player.map((p, i) => (

                      <div className="member-item" key={i} style={count.host_mno == p.mno ? { color: 'red ' } : {}} >
                        <img
                          src={playicon}       // 백엔드에 이미지 없어 임시 프로필만 표시
                          alt="프로필"
                          className="member-img"
                        />
                        {p.mname}   {count.host_mno == p.mno ? '방장' : ''}  </div>
                    ))
                  ) : (
                    <span className="offline">접속자가 없습니다.</span>
                  )}
                  <div className="drawer-footer">
                    <button className="drawer-exit-btn" onClick={퇴장}>
                      방 나가기
                    </button>
                  </div>
                </div>
              </Box>
            </Drawer>
          </div>


          <div className="chat-messages">
            {chatprint.map((c, index) => (
              <div
                key={index}
                className={`chat-item ${c.mname === auth?.mname ? "chat-my" : ""
                  } ${c.mname === "alarm" ? "chat-system" : ""}`}
              >
                {c.mname === "alarm" ? (
                  <div className="chat-system-message">{c.mmessage}</div>
                ) : (
                  <>
                    <div className="chat-name">{c.mname}
                      {c.mno === count.host_mno && <span className="host-badge">방장</span>}
                    </div>

                    {/*  말풍선 + 시간 같이 묶기 */}
                    <div className="chat-bubble-wrapper">
                      <div className="chat-bubble">{c.mmessage}</div>
                      {c.cdate && (
                        <div className="chat-time">
                          {new Date(c.cdate).toLocaleTimeString("ko-KR", {
                            hour: "2-digit",
                            minute: "2-digit",
                          })}
                        </div>
                      )}
                    </div>
                  </>
                )}
              </div>
            ))}
          </div>


          <div className="chat-input-area">
            {!readonly ? (
              <>
                <input
                  className="chat-input"
                  value={mmessage}
                  onChange={(e) => setmmessage(e.target.value)}
                  placeholder="메세지를 입력해주세요."
                  onKeyDown={(e) => e.key === "Enter" && textbtn()}
                />
                <button className="chat-btn" onClick={textbtn}>
                  ▶
                </button>
              </>
            ) : (
              <div className="readonly-box">
                방장님이 나가 채팅이 잠겼습니다.
              </div>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}
