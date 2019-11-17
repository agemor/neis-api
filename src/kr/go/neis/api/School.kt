package kr.go.neis.api

import java.net.URLEncoder
import java.net.URL

/**
 * NEIS API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 4.0
 */
class School {

    /**
     * 불러올 교육청 소속 교육기관의 종류
     */
    enum class Type(val id: Int) {
        /* 병설유치원 */ KINDERGARTEN(1),
        /* 초등학교 */ ELEMENTARY(2),
        /* 중학교 */ MIDDLE(3),
        /* 고등학교 */ HIGH(4)
    }

    /**
     * 불러올 교육기관의 관할 교육청
     */
    enum class Region(val url: String) {
        /* 서울 */ SEOUL("sen.go.kr"),
        /* 인천 */ INCHEON("ice.go.kr"),
        /* 부산 */ BUSAN("pen.go.kr"),
        /* 광주 */ GWANGJU("gen.go.kr"),
        /* 대전 */ DAEJEON("dje.go.kr"),
        /* 대구 */ DAEGU("dge.go.kr"),
        /* 세종 */ SEJONG("sje.go.kr"),
        /* 울산 */ ULSAN("use.go.kr"),
        /* 경기 */ GYEONGGI("goe.go.kr"),
        /* 강원 */ KANGWON("kwe.go.kr"),
        /* 충북 */ CHUNGBUK("cbe.go.kr"),
        /* 충남 */ CHUNGNAM("cne.go.kr"),
        /* 경북 */ GYEONGBUK("gbe.go.kr"),
        /* 경남 */ GYEONGNAM("gne.go.kr"),
        /* 전북 */ JEONBUK("jbe.go.kr"),
        /* 전남 */ JEONNAM("jne.go.kr"),
        /* 제주 */ JEJU("jje.go.kr")
    }

    /**
     * 교육기관의 종류
     */
    var type: Type = Type.HIGH;

    /**
     * 교육기관 관할 지역
     */
    var region: Region = Region.SEOUL;

    /**
     * 교육기관 고유 코드
     */
    var code: String = "";

    /**
     * 캐시용 해시맵
     */
    private val monthlyMenuCache = hashMapOf<Int, List<Menu>>()
    private val monthlyScheduleCache = hashMapOf<Int, List<Schedule>>()

    /**
     * 불러올 교육기관의 정보를 설정합니다.
     *
     * @param type   교육기관의 종류입니다. (School.Type 에서 병설유치원, 초등학교, 중학교, 고등학교 중 선택)
     * @param region 관할 교육청의 위치입니다. (School.Region 에서 선택)
     * @param code   교육기관의 고유 코드입니다.
     */
    constructor(type: Type, region: Region, code: String) {
        this.type = type
        this.region = region
        this.code = code
    }

    companion object {
        /**
         * NEIS 시스템에서 교육기관 정보를 조회합니다.
         *
         * @param region 교육기관 소재지
         * @param name 교육기관 명칭
         */
        fun find(region: School.Region, name: String): School {

            val url = URL("https://par.${region.url}/spr_ccm_cm01_100.do?kraOrgNm=${URLEncoder.encode(name, "utf-8")}&")

            // 원본 데이터는 JSON형식으로 이루어져 있습니다.
            var content = before(after(url.readText(), "orgCode"), "schulCrseScCodeNm")

            // 기관 종류와 코드를 구합니다.
            var typeIndex = before(after(content, "schulCrseScCode\":\""), "\"").toInt() - 1

            val type = School.Type.values()[typeIndex]
            val code = content.substring(3, 13)

            return School(type, region, code)
        }
    }


    /**
     * 월간 급식 메뉴를 불러옵니다.
     *
     * @param year  해당 년도를 yyyy 형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 급식메뉴 리스트
     */
    fun getMonthlyMenu(year: Int, month: Int): List<Menu> {

        val cacheKey = year * 12 + month

        if (this.monthlyMenuCache.contains(cacheKey))
            return this.monthlyMenuCache.getOrDefault(cacheKey, emptyList())

        val url = URL(
            "https://stu.${region.url}/sts_sci_md00_001.do" +
                    "?schulCode=${code}" +
                    "&schulCrseScCode=${type.id}" +
                    "&schulKndScCode=0${type.id}" +
                    "&schYm=${year}${String.format("%02d", month)}&"
        )

        var content = before(after(url.readText(), "<tbody>"), "</tbody>")

        // 리턴하기 전 캐시에 데이터를 저장합니다.
        val monthlyMenu = MenuParser.parse(content)
        monthlyMenuCache[cacheKey] = monthlyMenu

        return monthlyMenu
    }

    /**
     * 월간 학사 일정을 불러옵니다.
     *
     * @param year  해당 년도를 yyyy 형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 학사일정 리스트
     */
    fun getMonthlySchedule(year: Int, month: Int): List<Schedule> {

        val cacheKey = year * 12 + month

        if (this.monthlyScheduleCache.contains(cacheKey))
            return this.monthlyScheduleCache.getOrDefault(cacheKey, emptyList())

        val url = URL(
            "https://stu.${region.url}/sts_sci_sf01_001.do" +
                    "?schulCode=${code}" +
                    "&schulCrseScCode=${type.id}" +
                    "&schulKndScCode=0${type.id}" +
                    "&ay=${year}" +
                    "&mm=${String.format("%02d", month)}&"
        )

        var content = before(after(url.readText(), "<tbody>"), "</tbody>")

        val monthlySchedule = ScheduleParser.parse(content)
        this.monthlyScheduleCache[cacheKey] = monthlySchedule

        return monthlySchedule
    }

    fun clearCache() {
        this.monthlyScheduleCache.clear()
        this.monthlyMenuCache.clear()
    }


}