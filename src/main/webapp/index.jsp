<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title> 무브온 : 생활편의통합 플랫폼 </title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
    <div id="wrap">
        <jsp:include page="/header.jsp"></jsp:include>
        <div id="container">
            <div id="headerBox">
                <img src="favicon.ico" class="logo">
                <div>
                    <div id="title"> mOveOn </div>
                    <div id="sub-title"> 생활편의통합 플랫폼 </div>
                </div>

            </div>

            <div class="weatherBox">
                <div class="weather">
                    <div class="addr">인천 부평구 (테스트용 샘플 데이터)</div>
                    <div class="t1h">30°</div>
                    <div class="pty">맑음</div>

                    <div class="weatherDetails">
                        <div class="item">
                            <span class="label">습도</span>
                            <span>86%</span>
                        </div>
                        <div class="item">
                            <span class="label">하늘</span>
                            <span>맑음</span>
                        </div>
                        <div class="item">
                            <span class="label">풍속</span>
                            <span>5m/s</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="infoBox" style="justify-content: space-evenly;">
                <div class="btnBox1">
                    <div><a href="/living/trash.jsp"> <img src="img/recycling_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" class="sample"><br/> 쓰레기 배출정보</a></div>
                    <div><a href="/living/clothing_bin.jsp"> <img src="img/apparel_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" class="sample"><br/>의류수거함 위치정보 </a></div>
                </div>
                <div class="btnBox2">
                    <div><a href="/safety/criminal.jsp"> <img src="img/crisis_alert_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" class="sample"><br/> 성범죄자 위치정보 </a></div>
                    <div><a href="/transport/station.jsp"> <img src="img/elevator_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" class="sample"><br/>지하철 엘리베이터 </a></div>
                </div>

            </div>
        </div>
    </div>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&libraries=services"></script>
    <script src='/js/index.js'></script>
</body>
</html>