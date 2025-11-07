import Footer from "../Footer"
import Header from "../Header"
import "../../assets/css/community/newcreate.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState , useEffect } from "react";

export default function Newcreate(props){
    console.log('Newcreate.jsx');
    const nav = useNavigate();

    const [ createtitle , setcreatetitle ] = useState("");
    const [ createcontent , setcreatecontent ] = useState("");
    const [ total  , settotal ] = useState("2");
    const [ auth , setAuth ] = useState( { check : null } );

    // 로그인 정보 가져오기 쿠키 
    const checkcookie = async() => {
        try{
            const res = await axios.get("http://localhost:8080/api/member/info" ,
                { withCredentials : true } );
                setAuth(res.data);
                console.log(res.data);
                if(res.data === null){
                    alert('로그인후 이용해주세요');
                    nav('/login');
                }
        } catch(e) {setAuth( { check : false } ) };
    }


    useEffect( ()=> {checkcookie() } , [] );

    const addbtn = async() => {
        console.log('등록테스트');
         if(!createtitle.trim() || !createcontent.trim() ){
                alert('비워두지마세요.');
                return;
            }
            const obj = {btitle : createtitle , bcontent : createcontent , btotal : total }

        try{
        
            const response = await axios.post("http://localhost:8080/group/create" , obj , { withCredentials : true }  )
            const data =  response.data;

            if(data == true){
                alert('등록성공');
                nav("/community/bulkBuy");
        }else{
            alert('등록실패');
        }
        }catch(e){console.log(e)};
        
    }

    return(<>
            <Header/>
            <div id="main2">
            <input value={createtitle} type="text" id="createtitle" placeholder="제목을 입력해주세요."
            onChange={ (e) => setcreatetitle(e.target.value) } /><br/>
            <textarea value={createcontent} type="text" id="createcontent" placeholder="내용을 입력해주세요."
            onChange={ (e) => setcreatecontent(e.target.value) } />
            <select value={total} onChange={ (e) => settotal(e.target.value) } >
                <option>2</option>
                <option>3</option>
                <option>4</option>
                <option>5</option>
                <option>6</option>
                <option>7</option>
                <option>8</option>
                <option>9</option>
                <option>10</option>
            </select>
            <button onClick={addbtn} type="button"> 글등록 </button>
            </div>
            <Footer/>
        </>)
}