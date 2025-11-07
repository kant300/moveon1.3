<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title> 무브온 : 생활편의통합 플랫폼 </title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
</head>
<body>
    <div id="wrap">
        <jsp:include page="/header.jsp"></jsp:include>
        <div id="container">
            <div id="map">
                <div class="mapText">지하철 엘리베이터/에스컬레이터 위치 정보</div>
                <button class="mapLocation" type="button" onclick="moveToInitialPosition()"><img src="/img/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" /></button>
            </div>
        </div>
        <jsp:include page="/footer.jsp"></jsp:include>
    </div>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&libraries=services,clusterer"></script>
    <script src="/js/transport/station.js"></script>
</body>
</html>

