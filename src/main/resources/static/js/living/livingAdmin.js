
console.log('livingAdmin.js open');

// [*] 썸머노트 실행
$(document).ready(function() {
            $('#summernote').summernote({
                lang: 'ko-KR', // 썸머노트 메뉴들을 한글화 속성
                minHeight: 300, // 썸머노트 구역 최소높이    
                placeholder : '배출정보를 입력해주세요.', // 가이드 라인
            });
        });

// [1] 쓰레기 정보 등록
const trashAdd = async() => {
    let tno = '';
    let tday = '';
    let tcity = document.querySelector('.tCity').value;
    let tgu = document.querySelector('.tGu').value;
    let tinfo = document.querySelector('.tInfo').value;

    // 유효성 검사 (시,구,배출정보 미입력 시)
    if( !tcity || !tgu || !tinfo ){
        alert('지역 시(군), 구(읍), 배출 정보를 모두 입력해 주세요.');
        return;
    }

    let obj = { tno, tcity , tgu , tinfo , tday }; console.log(obj);

    let option = {
        method : "POST",
        headers : { "content-type" : "application/json" },
        body : JSON.stringify(obj)
    }
    try{ 
        const response = await fetch( "/living/trash" , option ); 
        const data = await response.json(); 
        console.log(response);
        console.log(data);
        if( data == true ){
            alert('등록성공');
            // 등록 성공시 
            // 시/도, 구 셀렉 값 초기화
            document.querySelector('.tCity').value = '';
            guSelect.innerHTML = '<option value="" disabled selected>시/군/구를 선택하세요.</option>';
            // 썸머노트 내용 초기화
            $('#summernote').summernote('reset');
            // 등록 후 새로조회
            trashPrint(); 
        }else{
            alert('등록실패');
        }
    }catch(e){console.log(e);}

}

// [1-1] 등록 드롭다운 변경 

 const cityData = {
    "서울특별시": [
      "종로구", "중구", "용산구", "성동구", "광진구",
      "동대문구", "중랑구", "성북구", "강북구", "도봉구",
      "노원구", "은평구", "서대문구", "마포구", "양천구",
      "강서구", "구로구", "금천구", "영등포구", "동작구",
      "관악구", "서초구", "강남구", "송파구", "강동구"
    ],
    "부산광역시": [
      "중구", "서구", "동구", "영도구", "부산진구",
      "동래구", "남구", "북구", "해운대구", "사하구",
      "금정구", "강서구", "연제구", "수영구", "사상구",
      "기장군"
    ],
    "대구광역시": [
      "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"
    ],
    "인천광역시": [
      "중구", "동구", "미추홀구", "연수구", "남동구",
      "부평구", "계양구", "서구", "강화군", "옹진군"
    ],
    "광주광역시": [
      "동구", "서구", "남구", "북구", "광산구"
    ],
    "대전광역시": [
      "동구", "중구", "서구", "유성구", "대덕구"
    ],
    "울산광역시": [
      "중구", "남구", "동구", "북구", "울주군"
    ],
    "세종특별자치시": [
      "세종특별자치시"
    ],
    "경기도": [
      "수원시 장안구", "수원시 권선구", "수원시 팔달구", "수원시 영통구",
      "성남시 수정구", "성남시 중원구", "성남시 분당구",
      "의정부시", "안양시 만안구", "안양시 동안구", "부천시 원미구",
      "부천시 소사구", "부천시 오정구", "광명시", "평택시", "동두천시",
      "안산시 상록구", "안산시 단원구", "고양시 덕양구", "고양시 일산동구", "고양시 일산서구",
      "과천시", "구리시", "남양주시", "오산시", "시흥시",
      "군포시", "의왕시", "하남시", "용인시 처인구", "용인시 기흥구", "용인시 수지구",
      "파주시", "이천시", "안성시", "김포시", "화성시",
      "광주시", "양주시", "포천시", "여주시", "연천군",
      "가평군", "양평군"
    ],
    "강원도": [
      "춘천시", "원주시", "강릉시", "동해시", "태백시",
      "속초시", "삼척시", "홍천군", "횡성군", "영월군",
      "평창군", "정선군", "철원군", "화천군", "양구군",
      "인제군", "고성군", "양양군"
    ],
    "충청북도": [
      "청주시 상당구", "청주시 서원구", "청주시 흥덕구", "청주시 청원구",
      "충주시", "제천시", "보은군", "옥천군", "영동군",
      "진천군", "괴산군", "음성군", "단양군"
    ],
    "충청남도": [
      "천안시 동남구", "천안시 서북구", "공주시", "보령시", "아산시",
      "서산시", "논산시", "계룡시", "당진시", "금산군",
      "부여군", "서천군", "청양군", "홍성군", "예산군", "태안군"
    ],
    "전라북도": [
      "전주시 완산구", "전주시 덕진구", "군산시", "익산시",
      "정읍시", "남원시", "김제시", "완주군", "진안군",
      "무주군", "장수군", "임실군", "순창군", "고창군", "부안군"
    ],
    "전라남도": [
      "목포시", "여수시", "순천시", "나주시", "광양시",
      "담양군", "곡성군", "구례군", "고흥군", "보성군",
      "화순군", "장흥군", "강진군", "해남군", "영암군",
      "무안군", "함평군", "영광군", "장성군", "완도군",
      "진도군", "신안군"
    ],
    "경상북도": [
      "포항시 남구", "포항시 북구", "경주시", "김천시", "안동시",
      "구미시", "영주시", "영천시", "상주시", "문경시",
      "경산시", "군위군", "의성군", "청송군", "영양군",
      "영덕군", "청도군", "고령군", "성주군", "칠곡군",
      "예천군", "봉화군", "울진군", "울릉군"
    ],
    "경상남도": [
      "창원시 의창구", "창원시 성산구", "창원시 마산합포구", "창원시 마산회원구", "창원시 진해구",
      "진주시", "통영시", "사천시", "김해시", "밀양시",
      "거제시", "양산시", "의령군", "함안군", "창녕군",
      "고성군", "남해군", "하동군", "산청군", "함양군",
      "거창군", "합천군"
    ]
  };

  const citySelect = document.getElementById('tCitySelect');
  const guSelect = document.getElementById('tGuSelect');

  function loadCityOptions() {
    citySelect.innerHTML = '<option value="" disabled selected>시/도를 선택하세요.</option>';
    for (const city in cityData) {
      const option = document.createElement('option');
      option.value = city;
      option.textContent = city;
      citySelect.appendChild(option);
    }
  }

  citySelect.addEventListener('change', () => {
    const selectedCity = citySelect.value;
    guSelect.innerHTML = '<option value="" disabled selected>시/군/구를 선택하세요.</option>';
    if (selectedCity && cityData[selectedCity]) {
      cityData[selectedCity].forEach(area => {
        const option = document.createElement('option');
        option.value = area;
        option.textContent = area;
        guSelect.appendChild(option);
      });
    }
  });

  loadCityOptions();

  // [2-1] 사용자 요청한 URL 에서 pno 가져오기
  const params = new URL(location.href).searchParams;
  
  const pno = params.get('pno'); console.log( pno );
  const page = params.get('page') || 1; // page가 존재하지 않으면 1
  
  // [2-2] 요청 매개변수를 이용한 fetch 쓰레기정보 게시물 요청하기
  const trashPrint = async () => { console.log('trashPrint.exe');

    try{// 1) fetch 이용한 쓰레기정보 게시물 요청
        const url = `/living/trash?pNo=${pno}&page=${page}`;
        const response = await fetch (url); // get 방식 option 생략
        const data = await response.json(); console.log(data); // data <--> pageDto
        // 2) fetch 결과를 테이블에 출력하기 
        const printTbody = document.querySelector('#printTbody')
        let html= ''; // 초기값 설정
        // 3) fetch로 부터 받은 데이터를 html 문자열로 반환
          data.data.forEach( ( dto ) => {
            html +=
               `<tr>
                    <td> ${ dto.tno }</td>
                    <td> ${ dto.tcity }</td>
                    <td> ${ dto.tgu}</td>
                    <td id="info"> ${ dto.tinfo }</td>
                    <td> ${ dto.tday }</td>
                    <td>  
                      <button type="button" onclick="trashUpdateMove('${dto.tcity}','${dto.tgu}')">수정</button>
                      <button type="button" onclick="trashDelete(${dto.tno})">삭제</button>
                    </td>
                </tr>`
          })
          // 4) 출력
          printTbody.innerHTML = html;
          // 5) 페이징 버튼 출력 함수 호출
          pageButtons( data );
        
    }catch( e ){ console.log( e ); }

  }

  // [2-3] 페이징 버튼 출력 함수 
  const pageButtons = async( data ) =>{
    
    // 백엔드로 부터 받은 pageDto{} <----> data{}
    let currentPage = parseInt( data.currentPage ); // parseInt( 자료 ) : 자료를 int타입으로 변환
    let totalPage = data.totalPage;
    let startBtn = data.startBtn;
    let endBtn = data.endBtn;

    // 페이징 처리할 위치 
    const pageBtnBox = document.querySelector('.pageBtnBox');
    let html = ''; // 초기값 설정

    // 이전 버튼 , 유효성검사) 만약에 현재페이지가 1이하면 1로 고정
    html += `<li class="pageBtn"><a href="/living/livingAdmin.jsp?pno=${pno}&page=${currentPage <= 1 ? 1 : currentPage-1}">이전</a></li>`
    // 페이지 버튼
    for( let i = startBtn ; i <= endBtn ; i ++){
      html += `<li><a href="/living/livingAdmin.jsp?pno=${pno}&page=${i}"
      style="${ i == currentPage ? 'color : #2195f3ff; font-weight : bold;' : ''}">
      ${i}
      </a></li>`
    }
    // 다음 버튼 , 유효성검사) 만약에 다음페이지가 전체페이지수 보다 커지면 전체페이지수로 고정
    html += `<li class="pageBtn"><a href="/living/livingAdmin.jsp?pno=${pno}&page=${currentPage+1 >= totalPage ? totalPage : currentPage+1}">다음</a></li>`
    pageBtnBox.innerHTML = html;

  } // func end
trashPrint(); // 최초 1번 실행

// // [2] 전체조회 
// const trashPrint = async() =>{
//   console.log('trashPrint.exe');
//   // 1. fetch option GET 생략
//   const response = await fetch("/living/trash")
//   // 2. 응답자료 타입변환
//   const data = await response.json();
//   // 3. 어디에
//   const printTbody = document.querySelector('#printTbody')
//   // 4. 무엇을
//   let html = ''; // 초기값 설정
//   for( let i = 0; i < data.length ; i++ ){
//     const dto = data[i]; // i번째 dto 꺼낸다.
//     html += `<tr>
//             <td> ${ dto.tno }</td>
//             <td> ${ dto.tcity }</td>
//             <td> ${ dto.tgu}</td>
//             <td id="info"> ${ dto.tinfo }</td>
//             <td> ${ dto.tday }</td>
//             <td>  
//               <button type="button" onclick="trashUpdateMove('${dto.tcity}','${dto.tgu}')">수정</button>
//               <button type="button" onclick="trashDelete(${dto.tno})">삭제</button>
//             </td>
//         </tr>`
//   }
//   // 5. 출력
//   printTbody.innerHTML = html;
// }
// trashPrint(); // 최초 1번 실행

// [3] 수정페이지로 이동
const trashUpdateMove = async( tcity,tgu ) =>{
  console.log('trashUpdate.exe');
  location.href=`/living/livingAdminUpdate.jsp?tCity=${tcity}&tGu=${tgu}`
}



// [4] 삭제
const trashDelete = async( tno ) =>{
  console.log('trashDelete.exe');
    // 1. fetch 
    const option = { method : "DELETE"}
    const response = await fetch( `/living/trash?tNo=${tno}`,option);
    const data = await response.json();
    // 2. fetch 응답
    if( data == true ){
        alert('삭제 성공');
        trashPrint();
    }else{
        alert('삭제 실패');
    }
  
  
}

































    