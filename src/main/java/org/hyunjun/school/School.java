package org.hyunjun.school;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 */
public class School {

    /**
     * 불러올 교육청 소속 교육기관의 종류
     */
    public enum Type {
        /* 병설유치원 */ KINDERGARTEN("1"),
        /* 초등학교 */ ELEMENTARY("2"),
        /* 중학교 */ MIDDLE("3"),
        /* 고등학교 */ HIGH("4");

        private String id;

        Type(String id) {
            this.id = id;
        }
    }

    /**
     * 불러올 교육기관의 관할 지역 (교육청)
     */
    public enum Region {

        /* 서울 */ SEOUL("hes.sen.go.kr"),
        /* 인천 */ INCHEON("hes.ice.go.kr"),
        /* 부산 */ BUSAN("hes.pen.go.kr"),
        /* 광주 */ GWANGJU("hes.gen.go.kr"),
        /* 대전 */ DAEJEON("hes.dje.go.kr"),
        /* 대구 */ DAEGU("hes.dge.go.kr"),
        /* 세종 */ SEJONG("hes.sje.go.kr"),
        /* 울산 */ ULSAN("hes.use.go.kr"),
        /* 경기 */ GYEONGGI("hes.goe.go.kr"),
        /* 강원 */ KANGWON("hes.kwe.go.kr"),
        /* 충북 */ CHUNGBUK("hes.cbe.go.kr"),
        /* 충남 */ CHUNGNAM("hes.cne.go.kr"),
        /* 경북 */ GYEONGBUK("hes.gbe.go.kr"),
        /* 경남 */ GYEONGNAM("hes.gne.go.kr"),
        /* 전북 */ JEONBUK("hes.jbe.go.kr"),
        /* 전남 */ JEONNAM("hes.jne.go.kr"),
        /* 제주 */ JEJU("hes.jje.go.kr");

        private String url;

        Region(String url) {
            this.url = url;
        }
    }

    private static final String MONTHLY_MENU_URL = "sts_sci_md00_001.do";
    private static final String SCHEDULE_URL = "sts_sci_sf01_001.do";

    /**
     * 교육기관의 종류
     */
    public Type schoolType;

    /**
     * 교육기관 관할 지역
     */
    public Region schoolRegion;

    /**
     * 교육기관 고유 코드
     */
    public String schoolCode;

    /**
     * Schooler를 초기화합니다.
     *
     * @param schoolType 교육기관의 종류입니다. (School.Type에서 병설유치원, 초등학교, 중학교, 고등학교 중 선택)
     * @param schoolRegion 관할 교육청의 위치입니다. (School.Region 에서 선택)
     * @param schoolCode 교육기관의 고유 코드입니다.
     */
    public School(Type schoolType, Region schoolRegion, String schoolCode) {
        this.schoolType = schoolType;
        this.schoolRegion = schoolRegion;
        this.schoolCode = schoolCode;
    }

    /**
     * 월간 급식 메뉴를 불러옵니다.
     *
     * @param year 해당 년도를 yyyy형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 급식메뉴 리스트
     */
    public List<SchoolMenu> getMonthlyMenu(int year, int month) {
        String targetUrl = "http://" + schoolRegion.url + "/" + MONTHLY_MENU_URL;

        try {
            Document menuData = Jsoup.connect(targetUrl)
                .data("schulCode", schoolCode)
                .data("schulCrseScCode", schoolType.id)
                .data("schulKndScCode", "0" + schoolType.id)
                .data("schYm", year + String.format("%02d", month)).post();

        return SchoolMenuParser.parse(menuData);

    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;

    }

    /**
     * 월간 학사 일정을 불러옵니다.
     *
     * @param year 해당 년도를 yyyy형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 학사일정 리스트
     */
    public List<SchoolSchedule> getMonthlySchedule(int year, int month) {

        String targetUrl = "http://" + schoolRegion.url + "/" + SCHEDULE_URL;

        try {
            Document scheduleData = Jsoup.connect(targetUrl)
                    .data("schulCode", schoolCode)
                    .data("schulCrseScCode", schoolType.id + "")
                    .data("schulKndScCode", "0" + schoolType.id)
                    .data("ay", "" + year)
                    .data("mm", String.format("%02d", month)).post();

            return SchoolScheduleParser.parse(scheduleData);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
    }
}
