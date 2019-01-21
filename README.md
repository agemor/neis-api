# NEIS API
> 빠르고 가벼운 전국 초,중,고등학교 급식 식단표/학사일정 파서

나이스(교육행정정보시스템) 학생서비스 페이지 파싱을 통해 월간 **학사일정**과 **급식 식단표**를 간편하게 불러올 수 있게 해 주는 API입니다. 한 번 로드된 데이터는 캐시하여 관리하고, 의존 라이브러리가 없어 9KB 정도의 적은 용량만을 차지하기 때문에 빠르고 가볍게 동작합니다.

## 설치하기
최신 아카이브 파일을 다운로드하여 프로젝트에 추가하거나, 소스 코드를 프로젝트에 포함하여 설치합니다.

-  [school-api-3.0.5.jar](https://github.com/agemor/school-api/releases/download/3.0.5/school-api-3.0.5.jar)

## 사용 예시

#### 코드

```java
School api = new School(School.Type.HIGH, School.Region.SEOUL, "B100000465");

try {
    List<SchoolMenu> menu = api.getMonthlyMenu(2019, 1);
    List<SchoolSchedule> schedule = api.getMonthlySchedule(2018, 12);

    // 2019년 1월 2일 점심 급식 식단표
    System.out.println(menu.get(1).lunch);

    // 2018년 12월 5일 학사일정
    System.out.println(schedule.get(4));

} catch (SchoolException e) {
    e.printStackTrace();
}

```

#### 출력
```
칼슘찹쌀밥
옥수수스프(빠네)1.2.5.6.13.
경양식돈까스1.2.5.6.10.12.13.
마카로니사과샐러드1.5.6.13.
모듬피클13.
복숭아플리또1.2.5.11.13.
단호박범벅1.5.13.

자치
생명존중자살예방교육
대학입시제도의이해
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

학교의 고유 코드는 [여기](https://code.schoolmenukr.ml/)에서 학교명으로 검색할 수 있습니다.
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

월간 급식 메뉴는 `getMonthlyMenu(int year, int month)`로 불러올 수 있습니다.

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

## 변경 사항
3.0.3 - 문제 상황에 알맞는 Exeption이 발생합니다.

## 기여하기
교육청 내부 URL 이동, HTML 구조 변경 등으로 파싱이 되지 않거나 에러가 발생할 수 있습니다. 이런 상황이 발생할 경우 이슈로 등록해 주시거나, 문제가 되는 부분을 수정하신 후 PR해 주시면 감사하겠습니다.

## 라이센스
이 소프트웨어는 [MIT 라이센스](https://github.com/agemor/school-api/blob/master/LICENSE)를 따라 자유롭게 이용하실 수 있습니다.

