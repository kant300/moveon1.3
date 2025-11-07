import{ useEffect, useState } from 'react';
import axios from 'axios';
import { Form, Link, useNavigate } from 'react-router-dom'
import '../assets/css/header.css'
import login from '../assets/images/icons/login_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.svg'
import React, { use } from "react";


import notice from '../assets/images/icons/notification_sound_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'



// Header
export default function Header() {

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
    useEffect( () => { getMyInfo(); } , [] );
    
    const navigate = useNavigate();

       return (<>
        <div id="header">
            <Link to='/'>mOveOn</Link>
            <div id= "headerRight">
                { member ? <>                 
                        <Link to='/notification'> {/*알림버튼*/}
                            <img src={notice} id="headerBtn" alt="알림" />
                        </Link> </> 
                        : <>                 
                        <Link to='/login'>{/*로그인버튼*/}
                            <img src={login} id="headerBtn" />
                        </Link> </>
                }
            </div>
        </div>
    </>)
}