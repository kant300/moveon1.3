<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>무브온 : 생활편의통합 플랫폼</title>
    <link rel="stylesheet" href="/css/safety/criminal.css">
</head>
<body>
    <div id="wrap">
        <jsp:include page="/header.jsp"></jsp:include>
        <div id="container">
            <div id="map">

                <div id="criminalTitle" class="mapText"> 성범죄자 위치 정보 
                <span class="box_search">
                    <input type="search" id="innerQuery" class="tf_keyword" placeholder="장소, 주소검색" maxlength="100">
                    <button type="button" class="search_button" style="background-color: #99E2FF;padding: 0px;"> 검색 </button>
                </span>
                <div id="criminal_container">
                    내 위치 반경 2km 내 등록된 성범죄자 인원수 : <span id="criminal_count"> 0 </span> 명
                </div>
            </div> 
            <button class="mapLocation" type="button" onclick="moveToInitialPosition()"><img src="/img/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" /></button>
            </div>
        </div>
        <jsp:include page="/footer.jsp"></jsp:include>
    </div>
    <!-- 카카오지도 API -->
    <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&libraries=services,geometry"></script>
    <script src='/js/safety/criminal.js'></script>
</body>
</html>