School API
===================

[School API](http://github.com/agemor/school-api)는 전국 교육청 학생 서비스 페이지(hes.xxx.go.kr)를 파싱하여 월간 **학사일정**과 **급식 메뉴**를 간편하게 불러옵니다.

> **불러올 수 있는 범위:**

>  전국의 모든 **교육청 소속 공/사립 교육기관**
>  
> - 병설유치원
> - 초등학교
> - 중학교
> - 고등학교
> 
> **Tip** 특정 학교가 교육청 소속 기관인지는 [여기](http://www.schoolinfo.go.kr)에서 확인할 수 있습니다.


###설치하기

[school.jar](https://github.com/agemor/school-api/raw/master/school.jar) 파일을 다운로드하여 프로젝트의 라이브러리에 추가합니다.

```java
import org.hyunjun.school.*;
```
를 했을 때 에러가 발생하지 않는다면 라이브러리에 올바르게 추가된 것입니다.


###학교 코드와 관할 지역 검색하기

데이터를 불러오기 위해서는 학교의 고유 코드를 알아야 합니다.
[학교 코드.xls](https://github.com/agemor/school-api/raw/master/%ED%95%99%EA%B5%90%20%EC%BD%94%EB%93%9C.xls) 파일을 다운로드하여 불러오고자 하는 학교의 코드를 확인합니다.
 
 학교 코드는 `X000000000` 형식의 10자리 문자열입니다.


###사용법

School API를 사용하기 위해 `School`인스턴스를 생성합니다.
```java
School api = new School(/* 학교 종류 */, /* 관할 지역 */, /* 학교 코드 */);
```

 학교 종류는 `School.Type` 에서 선택할 수 있습니다.

> - 병설유치원: `School.Type.KINDERGARTEN`
> - 초등학교: `School.Type.ELEMENTARY`
> - 중학교: `School.Type.MIDDLE`
> - 고등학교: `School.Type.HIGH`


관할 지역은 `School.Region` 에서 선택할 수 있습니다.

> - 서울특별시: `School.Region.SEOUL`
> - 인천광역시: `School.Region.INCHEON`
> - 부산광역시: `School.Region.BUSAN`
> - 광주광역시: `School.Region.GWANGJU`
> - 대전광역시: `School.Region.DAEJEON`
> - 대구광역시: `School.Region.DAEGU`
> - 세종특별자치시: `School.Region.SEJONG`
> - 울산광역시: `School.Region.ULSAN`
> - 경기도: `School.Region.GYEONGGI`
> - 강원도: `School.Region.KANGWON`
> - 충청북도: `School.Region.CHUNGBUK`
> - 충청남도: `School.Region.CHUNGNAM`
> - 경상북도: `School.Region.GYEONGBUK`
> - 경상남도: `School.Region.GYEONGNAM`
> - 전라북도: `School.Region.JEONBUK`
> - 전라남도: `School.Region.JEONNAM`
> - 제주도: `School.Region.JEJU`

예로 서울에 위치한 선덕고등학교를 설정해 보았습니다.

```java
School api = new School(School.Type.HIGH, School.Region.SEOUL, "B100000465");
```


월간 메뉴는 `getMonthlyMenu(int year, int month)`로 불러올 수 있습니다.



```java
List<SchoolMenu> menus = api.getMonthlyMenu(2015, 4);

for(int i = 0; i < menus.size(); i++) {
    System.out.println((i + 1) + "일 식단");
    System.out.println(menus.get(i));
}

// 24일 저녁 메뉴
System.out.println(menus.get(23).dinner);

// 1일 아침 메뉴
System.out.println(menus.get(0).breakfast);

// 30일 점심 메뉴
System.out.println(menus.get(29).lunch);

```

> **출력 예시**
> ```
> 보리밥
볶음짜장면⑤⑥⑩
떡만두국①⑤⑥⑩⑬
열무겉절이
김치볶음⑤⑨
구이김
> ```


월간 학사일정은 `getMonthlySchedule(int year, int month)`로 불러올 수 있습니다.

```java
List<SchoolSchedule> schedules = api.getMonthlySchedule(2015, 4);

for(int i = 0; i < schedules .size(); i++) {
    System.out.println((i + 1) + "일 학사일정");
    System.out.println(schedules .get(i));
}

// 5일 일정
System.out.println(menus.get(4).schedule);

// 13일 일정
System.out.println(menus.get(12).schedule);

```

> **출력 예시**
> ```
> 학력고사
> ```

###라이센스

이 소프트웨어는 [MIT 라이센스](#)를 따라 자유롭게 사용하실 수 있습니다.
