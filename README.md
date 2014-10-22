school-api
==========

전국 각 시, 도의 교육청 학생서비스 페이지(hes.xxx.go.kr)을 파싱하여 급식 식단표(월간)와 학사 일정(월간)을 간단하게 읽어올 수 있습니다. 


##읽어올 수 있는 범위
교육청에 등록되어있는 전국의 모든 공/사립 교육 기관
1. 병설유치원
2. 초등학교
3. 중학교
4. 고등학교

##사용 방법

    SchoolAPI.getMonthlyMenu(관할 교육청, 학교 코드, 학교 종류, 읽어올 해, 읽어올 달)

1. 관할 교육청(String)
    SchoolAPI.Country 를 통해 가능한 목록을 보실 수 있습니다.
    서울: Country.SEOUL
    울산: Country.ULSAN
    전북: Country.JEONBUK
    부산: Country.BUSAN
    세종: Country.SEJONG
    전남: Country.JEONNAM
    대구: Country.DAEGU
    경기: Country.GYEONGGI
    경북: Country.GYEONGBUK
    인천: Country.INCHEON
    강원: Country.KANGWON
    경남: Country.GYEONGNAM
    광주: Country.GWANGJU
    충북: Country.CHUNGBUK
    제주: Country.JEJU
    대전: Country.DAEJEON
    충남: Country.CHUNGNAM
    
2. 학교 코드(String)
    나이스(NEIS)에서 각 학교에 부여하는 코드입니다.
    X10000XXXX 의 형식을 하고 있습니다. 맨 첫 자리는 지역에 따라 다릅니다.
    구글에서 학교 코드 번호를 검색해서 찾거나 교육청 학교 검색 기능을 통해 찾을 수 있습니다.
    
3. 학교 종류(Int)
    유치원(1), 초등학교(2), 중학교(3), 고등학교(4) 중에 해당되는 것을 입력하면 됩니다.
    
    유치원: SchoolType.KINDERGARTEN
    초등학교: SchoolType.ELEMENRARY
    중학교: SchoolType.MIDDLE
    고등학교: SchoolType.HIGH
    
4. 읽어올 해(Int)
    4자리 (ex. 2014) 로 입력합니다.
    
5. 읽어올 달(Int)
    데이터를 읽어올 달을 입력합니다.
    
    
##출력 결과
급식의 경우, 
MenuData[] result = SchoolAPI.getMonthlyMenu(Country.SEOUL, "B100000465", SchoolType.HIGH, 2014, 10);
을 실행하게 되면, 서울에 위치한 선덕고등학교의 2014년 10월 급식 식단표를 배열 형태(MenuData[])로 가져오게 됩니다.
2014년 10월 1일 메뉴: result[0];
2014년 10월 10일 메뉴: result[9];
2014년 10월 31일 메뉴: result[30];

조식(String): result[날짜-1].breakfast
중식(String): result[날짜-1].lunch
석식(String): result[날짜-1].dinner
통째로 읽어오고 싶으면: result[날짜-1].toString()

급식이 없을 경우 "급식 없음" 이 입력됩니다.

학사 일정의 경우
ScheduleData[] result = SchoolAPI.getMonthlySchedule(Country.SEOUL, "B100000465", SchoolType.HIGH, 2014, 10);
2014년 10월 1일의 일정: result[0];
.. (이하 동일) ...

일정을 읽어올 때는 result[날짜-1].schedule 로 쓰면 됩니다.


##참고
학교용 앱을 만들다 이런 API를 필요로 하는 분이 많으실 것 같아 제가 직접 만들어 공개하게 되었습니다.
이 API는 html 파싱 라이브러리인 JSoup이 필요합니다. (www.jsoup.org)
