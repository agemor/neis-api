package org.hyunjun.school;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.0
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

        /* 서울 */ SEOUL("stu.sen.go.kr"),
        /* 인천 */ INCHEON("stu.ice.go.kr"),
        /* 부산 */ BUSAN("stu.pen.go.kr"),
        /* 광주 */ GWANGJU("stu.gen.go.kr"),
        /* 대전 */ DAEJEON("stu.dje.go.kr"),
        /* 대구 */ DAEGU("stu.dge.go.kr"),
        /* 세종 */ SEJONG("stu.sje.go.kr"),
        /* 울산 */ ULSAN("stu.use.go.kr"),
        /* 경기 */ GYEONGGI("stu.goe.go.kr"),
        /* 강원 */ KANGWON("stu.kwe.go.kr"),
        /* 충북 */ CHUNGBUK("stu.cbe.go.kr"),
        /* 충남 */ CHUNGNAM("stu.cne.go.kr"),
        /* 경북 */ GYEONGBUK("stu.gbe.go.kr"),
        /* 경남 */ GYEONGNAM("stu.gne.go.kr"),
        /* 전북 */ JEONBUK("stu.jbe.go.kr"),
        /* 전남 */ JEONNAM("stu.jne.go.kr"),
        /* 제주 */ JEJU("stu.jje.go.kr");

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
     * 불러올 학교 정보를 설정합니다.
     *
     * @param schoolType   교육기관의 종류입니다. (School.Type 에서 병설유치원, 초등학교, 중학교, 고등학교 중 선택)
     * @param schoolRegion 관할 교육청의 위치입니다. (School.Region 에서 선택)
     * @param schoolCode   교육기관의 고유 코드입니다.
     */
    public School(Type schoolType, Region schoolRegion, String schoolCode) {
        this.schoolType = schoolType;
        this.schoolRegion = schoolRegion;
        this.schoolCode = schoolCode;
    }

    /**
     * 월간 급식 메뉴를 불러옵니다.
     *
     * @param year  해당 년도를 yyyy 형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 급식메뉴 리스트
     */
    public List<SchoolMenu> getMonthlyMenu(int year, int month) throws SchoolException {

        StringBuffer targetUrl = new StringBuffer("https://" + schoolRegion.url + "/" + MONTHLY_MENU_URL);
        targetUrl.append("?");
        targetUrl.append("schulCode=" + schoolCode + "&");
        targetUrl.append("schulCrseScCode=" + schoolType.id + "&");
        targetUrl.append("schulKndScCode=" + "0" + schoolType.id + "&");
        targetUrl.append("schYm=" + year + String.format("%02d", month) + "&");

        try {
            String content = getContentFromUrl(new URL(targetUrl.toString()), "<tbody>", "</tbody>");
            return SchoolMenuParser.parse(content);
        } catch (MalformedURLException e) {
            throw new SchoolException("교육청 접속 주소가 올바르지 않습니다.");
        }
    }

    /**
     * 월간 학사 일정을 불러옵니다.
     *
     * @param year  해당 년도를 yyyy 형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 학사일정 리스트
     */
    public List<SchoolSchedule> getMonthlySchedule(int year, int month) throws SchoolException {

        StringBuffer targetUrl = new StringBuffer("https://" + schoolRegion.url + "/" + SCHEDULE_URL);
        targetUrl.append("?");
        targetUrl.append("schulCode=" + schoolCode + "&");
        targetUrl.append("schulCrseScCode=" + schoolType.id + "&");
        targetUrl.append("schulKndScCode=" + "0" + schoolType.id + "&");
        targetUrl.append("ay=" + year + "&");
        targetUrl.append("mm=" + String.format("%02d", month) + "&");

        try {
            String content = getContentFromUrl(new URL(targetUrl.toString()), "<tbody>", "</tbody>");
            return SchoolScheduleParser.parse(content);
        } catch (MalformedURLException e) {
            throw new SchoolException("교육청 접속 주소가 올바르지 않습니다.");
        }
    }

    private String getContentFromUrl(URL url, String readAfter, String readBefore) throws SchoolException {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuffer buffer = new StringBuffer();
            String inputLine;

            boolean reading = false;

            while ((inputLine = reader.readLine()) != null) {
                if (reading) {
                    if (inputLine.contains(readBefore))
                        break;
                    buffer.append(inputLine);
                } else {
                    if (inputLine.contains(readAfter))
                        reading = true;
                }
            }
            reader.close();
            return buffer.toString();

        } catch (IOException e) {
            throw new SchoolException("교육청 서버에 접속하지 못하였습니다.");
        }
    }
}
