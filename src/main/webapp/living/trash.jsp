<%@ page language = "java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv='X-UA-Compatible' content='IE=edge'>
    <title>무브온 : 생활편의통합 플랫폼</title>
    <meta name='viewport' content='width=device-width, initial-scale=1'>
    <link rel="stylesheet" href="/css/living/trash.css">
</head>
<body>

    <div id="wrap">
        <jsp:include page="/header.jsp"></jsp:include>
            <div id="container">
                    <!-- 각자가 들어갈 html 들 -->
                    <div style="min-height: 86.2vh; min-width: 399px; display: flex; flex-direction: column; align-items: center;">
                        <div style="font-weight: bold; font-size: 17px;padding: 5px;">쓰레기 배출정보</div><br>
                        <div id="textBox"> 위치 엑세스가 거부되었습니다. 엑세스를 허용해주세요. </div> </br></br>

                        <div id="infoBox"> 쓰레기 정보 </div>
                        <div class="floating-icon">
                            <a href="/living/livingAdmin.jsp?pno=1&page=1">
                                <img src="/img/manage_accounts_100dp_1F1F1F_FILL0_wght400_GRAD0_opsz48.svg" class="icon">
                            </a>
                        </div>
                    </div>
            </div>
        <jsp:include page="/footer.jsp"></jsp:include>
    </div>

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9eb4f86b6155c2fa2f5dac204d2cdb35&libraries=services"></script>
    <script src="/js/living/trash.js"></script>
</body>
</html>