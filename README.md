# NEIS API 

> 빠르고 가벼운 전국 초,중,고등학교 급식 식단표/학사일정 파서

나이스(교육행정정보시스템) 학생서비스 페이지 파싱을 통해 월간 **학사일정**과 **급식 식단표**를 간편하게 불러올 수 있게 해 주는 API입니다. 한 번 로드된 데이터는 캐시하여 관리하고, 의존 라이브러리가 없어 7KB 정도의 적은 용량만을 차지하기 때문에 빠르고 가볍게 동작합니다.

## 설치하기
`neis-api`는 [jcenter](https://bintray.com/agemor/neis-api/kr.go.neis.api)에 호스팅되어 있습니다.
```groovy
dependencies {
    implementation "kr.go.neis.api:neis-api:4.0.1"
}
```

## 사용 예시

#### 코드

##### Kotlin
```kotlin
val school = School.find(School.Region.SEOUL, "선덕고등학교")

// 2019년 1월 2일 점심 급식 식단표
val menu = school.getMonthlyMenu(2019, 1)
println(menu[1].lunch)

// 2018년 12월 5일 학사일정
val schedule = school.getMonthlySchedule(2018, 12)
println(schedule[4])
```

##### Java
```java
try {
    School school = School.Companion.find(School.Region.SEOUL, "선덕고등학교");

    List<Menu> menu = school.getMonthlyMenu(2019, 1);
    List<Schedule> schedule = school.getMonthlySchedule(2018, 12);

    // 2019년 1월 2일 점심 급식 식단표
    System.out.println(menu.get(1).getLunch());

    // 2018년 12월 5일 학사일정
    System.out.println(schedule.get(4));

} catch (NEISException e) {
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
NEIS API를 사용하기 위해 `School`인스턴스가 우선 생성되어야 합니다. 생성에는 세 가지 정보가 필요합니다.
```kotlin
val school = School(/* 학교 종류 */, /* 관할 지역 */, /* 학교 코드 */)
```
수동으로 코드를 입력할 필요 없이 교육기관명으로 자동 검색하는 방법도 있습니다. 이 방법은 교육기관 정보를 NEIS에서 검색하여 가져오므로 위의 방법보다 시간이 조금 더 소요됩니다.
```kotlin
val school = School.find(/* 관할 지역 */, /* 학교 이름 */)
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

학교의 고유 코드는 [여기](http://jubsoo2.bscu.ac.kr/src_gogocode/src_gogocode.asp)에서 학교명으로 검색할 수 있습니다.
 학교 코드는 `X000000000` 형식의 10자리 문자열입니다.

### 학사일정 불러오기
월간 학사일정은 `getMonthlySchedule(year:Int, month:Int)`로 불러올 수 있습니다. 불러온 학사일정은 ArrayList 형태로 저장됩니다. 날짜는 0일부터 시작합니다. (1일 = 0, 2일 = 1, ... 31일=30)

```kotlin
// 2018년 12월의 학사일정 불러오기
val schedule = school.getMonthlySchedule(2018, 12)

// 그 달의 모든 일정을 출력
for (i in schedule.indices) {
    println("${i + 1}일 학사일정")
    println(schedule[i])
}

// 15일 학사일정
println(schedule[14])
```

### 급식 식단 불러오기

월간 급식 메뉴는 `getMonthlyMenu(year:Int, month:Int)`로 불러올 수 있습니다.

```kotlin
// 2019년 1월의 급식 식단 불러오기
val menu = school.getMonthlyMenu(2019, 1)

// 그 달의 모든 식단을 출력
for (i in menu.indices) {
    println("${i + 1}일 메뉴")
    println(menu[i])
}
// 24일 저녁 메뉴
println(menu[23].dinner)

// 1일 아침 메뉴
println(menu[0].breakfast)

// 30일 점심 메뉴
println(menu[29].lunch)
```

### 사용 시 주의사항
NEIS API 내 모든 메서드들은 동기적(synchronous) IO를 사용합니다. 따라서 NEIS 서버에서 데이터를 로드하는 메서드 (`getMonthlyMenu`,`getMonthlySchedule`,`find`) 사용 시 스레드나 코루틴 등으로 병렬 처리를 해 주셔야 blocking 이 발생하지 않습니다.

## 기여하기
교육청 내부 URL 이동, HTML 구조 변경 등으로 파싱이 되지 않거나 에러가 발생할 수 있습니다. 이런 상황이 발생할 경우 이슈로 등록해 주시거나, 문제가 되는 부분을 수정하신 후 PR해 주시면 감사하겠습니다.

## 라이센스
이 라이브러리는 [MIT 라이센스](https://github.com/agemor/school-api/blob/master/LICENSE)를 따라 자유롭게 이용하실 수 있습니다.

