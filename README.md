# School API
> 빠르고 가벼운 전국 초,중,고등학교 학사일정/식단표 파서

[School API](http://github.com/agemor/school-api)는 전국 교육청 학생 서비스 페이지(stu.xxx.go.kr)를 파싱하여 월간 **학사일정**과 **식단표**를 간편하게 불러올 수 있게 해 줍니다. 별다른 의존 라이브러리 없이 독립적으로 동작하기 때문에 9KB 정도의 용량만을 차지합니다.

## 설치하기
[최신 아카이브 파일](https://github.com/agemor/school-api/blob/master/bin/schoolapi-3.0.2.jar)을 다운로드하여 프로젝트에 추가하거나, 소스 코드를 프로젝트에 포함하여 설치합니다.

-  [schoolapi-3.0.2.jar](https://github.com/agemor/school-api/blob/master/bin/schoolapi-3.0.2.jar)
-  [schoolapi-3.0.1.jar](https://github.com/agemor/school-api/blob/master/bin/schoolapi-3.0.1.jar)

## 사용 예시

#### 코드

```java
School api = new School(School.Type.HIGH, School.Region.SEOUL, "B100000465");

List<SchoolMenu> menu = api.getMonthlyMenu(2016, 4);
List<SchoolSchedule> schedule = api.getMonthlySchedule(2016, 4);

// 2016년 4월 22일 저녁 식단표
System.out.println(menu.get(21).dinner);

// 2016년 4월 16일 학사일정
System.out.println(schedule.get(15));
```

#### 출력
```
보리밥
볶음짜장면⑤⑥⑩
떡만두국①⑤⑥⑩⑬
열무겉절이
김치볶음⑤⑨
구이김
학력고사
```
## 사용 방법

### School 인스턴스 생성
School API를 사용하기 위해 `School`인스턴스를 생성합니다.
```java
School api = new School(/* 학교 종류 */, /* 관할 지역 */, /* 학교 코드 */);
```

#### 학교 종류

 학교 종류는 `School.Type` 에서 선택할 수 있습니다.

- 병설유치원: `School.Type.KINDERGARTEN`
- 초등학교: `School.Type.ELEMENTARY`
- 중학교: `School.Type.MIDDLE`
- 고등학교: `School.Type.HIGH`

#### 관할 지역

관할 지역은 `School.Region` 에서 선택할 수 있습니다.

- 서울특별시: `School.Region.SEOUL`
- 인천광역시: `School.Region.INCHEON`
- 부산광역시: `School.Region.BUSAN`
- 광주광역시: `School.Region.GWANGJU`
- 대전광역시: `School.Region.DAEJEON`
- 대구광역시: `School.Region.DAEGU`
- 세종특별자치시: `School.Region.SEJONG`
- 울산광역시: `School.Region.ULSAN`
- 경기도: `School.Region.GYEONGGI`
- 강원도: `School.Region.KANGWON`
- 충청북도: `School.Region.CHUNGBUK`
- 충청남도: `School.Region.CHUNGNAM`
- 경상북도: `School.Region.GYEONGBUK`
- 경상남도: `School.Region.GYEONGNAM`
- 전라북도: `School.Region.JEONBUK`
- 전라남도: `School.Region.JEONNAM`
- 제주도: `School.Region.JEJU`

#### 학교 코드

학교의 고유 코드는 [학교 코드.xls](https://github.com/agemor/school-api/raw/master/%ED%95%99%EA%B5%90%20%EC%BD%94%EB%93%9C.xls) 파일에서 확인하거나 [여기](http://www.schoolinfo.go.kr)에서 검색할 수 있습니다.
 학교 코드는 `X000000000` 형식의 10자리 문자열입니다.
 
### 학사일정 불러오기
월간 학사일정은 `getMonthlySchedule(int year, int month)`로 불러올 수 있습니다. 불러온 학사일정은 ArrayList 형태로 저장됩니다. 날짜는 0일부터 시작합니다. (1일 = 0, 2일 = 1, ... 31일=30)

```java
List<SchoolSchedule> scheduleList = api.getMonthlySchedule(2015, 4);

for(int i = 0; i < scheduleList.size(); i++) {
    System.out.println((i + 1) + "일 학사일정");
    System.out.println(scheduleList.get(i));
}

// 15일 학사일정
System.out.println(menuList.get(14).schedule);
```

### 급식 식단 불러오기

월간 메뉴는 `getMonthlyMenu(int year, int month)`로 불러올 수 있습니다.

```java
List<SchoolMenu> menuList = api.getMonthlyMenu(2015, 4);

for(int i = 0; i < menuList.size(); i++) {
    System.out.println((i + 1) + "일 식단");
    System.out.println(menuList.get(i));
}

// 24일 저녁 메뉴
System.out.println(menuList.get(23).dinner);

// 1일 아침 메뉴
System.out.println(menuList.get(0).breakfast);

// 30일 점심 메뉴
System.out.println(menuList.get(29).lunch);
```
 
## 기여하기
교육청 내부 URL 이동, HTML 구조 변경 등으로 파싱이 되지 않거나 에러가 발생할 수 있습니다. 이런 상황이 발생할 경우 이슈로 등록해 주시거나, 문제가 되는 부분을 수정하신 후 PR해 주시면 감사하겠습니다.

## 라이센스
이 소프트웨어는 [MIT 라이센스](https://github.com/agemor/school-api/blob/master/LICENSE)를 따라 자유롭게 이용하실 수 있습니다.

