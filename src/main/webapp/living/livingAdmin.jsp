<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>무브온 : 생활편의통합 플랫폼</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <!-- 제이쿼리 -->
    <script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
    <!-- 썸머노트 -->
    <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.js" integrity="sha512-qTQLA91yGDLA06GBOdbT7nsrQY8tN6pJqjT16iTuk08RWbfYmUz/pQD3Gly1syoINyCFNsJh7A91LtrLIwODnw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.css" integrity="sha512-rDHV59PgRefDUbMm2lSjvf0ZhXZy3wgROFyao0JxZPGho3oOuWejq/ELx0FOZJpgaE5QovVtRN65Y3rrb7JhdQ==" crossorigin="anonymous" referrerpolicy="no-referrer" /> -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-lite.min.js" integrity="sha512-sIOi8vwsJpzCHtHd06hxJa2umWfY1FfEEE0nGAaolGlR73EzNnQaWdijVyLueB0PSnIp8Mj2TDQLLHTiIUn1gw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-lite.min.css" integrity="sha512-ySljI0ZbsJxjJIpfsg+7ZJOyKzBduAajCJpc4mBiVpGDPln2jVQ0kwYu3e2QQM5fwxLp6VSVaJm8XCK9uWD4uA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <!-- 썸머노트 한글패치 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/lang/summernote-ko-KR.min.js" integrity="sha512-npFeJw8MO1QVbeFuD7rqVR1CpAbOnUMoYnZIxDbz58biKU52Unq7Av3cn+SZ2KD4yOLWj4bOcjIV1+d4aEXAyg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="/css/living/livingAdmin.css">
</head>
<body>
    <h3> 관리자 페이지 </h3> </br>
    <h3><a href="/index.jsp">메인페이지로 이동 </a></h3></br>
    
    <div class="addWrap">
        <div class="addContainer">
            <h3> 쓰레기 정보 등록 </h3> </br>
            <form id="trashForm">
            지역시 :
            <select class="tCity" id="tCitySelect">
            <option value="">시/도를 선택하세요.</option>
            <option value="서울특별시">서울특별시</option>
            <option value="인천광역시">인천광역시</option>
            <option value="경기도 부천시">경기도 부천시</option>
            </select> <br><br>
            지역구 :
            <select class="tGu" id="tGuSelect">
            <option value="">시/군/구를 선택하세요.</option>
            </select> <br><br>
            배출 정보 : <textarea class="tInfo" id="summernote" name="editoradata"></textarea> 
            <span><button type="button" onclick="trashAdd()">등록</button></span>
            </form>
        </div>    
    </div>
    
    <div class="printWrap">
        <div class="printContainer">
            <h3> 쓰레기 정보 목록 </h3> </br>
            <table>
                <thead>
                    <tr>
                        <th> 쓰레기 배출 정보 번호 </th> 
                        <th> 배출 지역 시/도 </th>
                        <th> 배출 지역 시/군/구 </th>
                        <th> 배출 정보</th>
                        <th> 처리날짜</th>
                        <th> 수정/삭제</th>
                    </tr>
                </thead>
                <tbody id="printTbody">
                </tbody>
            </table>
            <div style="width:200px;">
                </br>
                <ul class="pageBtnBox"
                    style="display: flex; justify-content: space-between;">
                    
                    <li><a href="/living/livingAdmin.jsp?pno=1&page=1"><span>1</span></a></li>
                    <li><a href="/living/livingAdmin.jsp?pno=1&page=1">2</a></li>
                    <li><a href="/living/livingAdmin.jsp?pno=1&page=1">3</a></li>
                    <li><a href="/living/livingAdmin.jsp?pno=1&page=1">4</a></li>
                    <li><a href="/living/livingAdmin.jsp?pno=1&page=1">5</a></li>
                </ul>
            </div>
        </div>    
    </div>

    <script src="/js/living/livingAdmin.js"></script>
</body>
</html>