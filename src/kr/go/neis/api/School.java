package kr.go.neis.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.1
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
    public Type type;

    /**
     * 교육기관 관할 지역
     */
    public Region region;

    /**
     * 교육기관 고유 코드
     */
    public String code;

    /**
     * 캐시용 해시맵
     */
    private Map<Integer, List<SchoolMenu>> monthlyMenuCache;
    private Map<Integer, List<SchoolSchedule>> monthlyScheduleCache;

    /**
     * 불러올 학교 정보를 설정합니다.
     *
     * @param type   교육기관의 종류입니다. (School.Type 에서 병설유치원, 초등학교, 중학교, 고등학교 중 선택)
     * @param region 관할 교육청의 위치입니다. (School.Region 에서 선택)
     * @param code   교육기관의 고유 코드입니다.
     */
    public School(Type type, Region region, String code) {
        this.type = type;
        this.region = region;
        this.code = code;
        this.monthlyMenuCache = new HashMap<>();
        this.monthlyScheduleCache = new HashMap<>();
    }

    /**
     * 월간 급식 메뉴를 불러옵니다.
     *
     * @param year  해당 년도를 yyyy 형식으로 입력. (ex. 2016)
     * @param month 해당 월을 m 형식으로 입력. (ex. 3, 12)
     * @return 각 일자별 급식메뉴 리스트
     */
    public List<SchoolMenu> getMonthlyMenu(int year, int month) throws SchoolException {

        int cacheKey = year * 12 + month;

        if (this.monthlyMenuCache.containsKey(cacheKey))
            return this.monthlyMenuCache.get(cacheKey);

        StringBuilder targetUrl = new StringBuilder();

        targetUrl.append("https://").append(region.url).append("/").append(MONTHLY_MENU_URL);
        targetUrl.append("?schulCode=").append(code);
        targetUrl.append("&schulCrseScCode=").append(type.id);
        targetUrl.append("&schulKndScCode=0").append(type.id);
        targetUrl.append("&schYm=").append(year).append(String.format("%02d", month));
        targetUrl.append("&");

        try {
            String content = getContentFromUrl(new URL(targetUrl.toString()), "<tbody>", "</tbody>");

            // 리턴하기 전 캐시에 데이터를 저장합니다.
            List<SchoolMenu> monthlyMenu = SchoolMenuParser.parse(content);
            this.monthlyMenuCache.put(cacheKey, monthlyMenu);

            return monthlyMenu;

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

        int cacheKey = year * 12 + month;

        if (this.monthlyScheduleCache.containsKey(cacheKey))
            return this.monthlyScheduleCache.get(cacheKey);

        StringBuilder targetUrl = new StringBuilder();

        targetUrl.append("https://").append(region.url).append("/").append(SCHEDULE_URL);
        targetUrl.append("?schulCode=").append(code);
        targetUrl.append("&schulCrseScCode=").append(type.id);
        targetUrl.append("&schulKndScCode=0").append(type.id);
        targetUrl.append("&ay=").append(year);
        targetUrl.append("&mm=").append(String.format("%02d", month));
        targetUrl.append("&");

        try {
            String content = getContentFromUrl(new URL(targetUrl.toString()), "<tbody>", "</tbody>");

            List<SchoolSchedule> monthlySchedule = SchoolScheduleParser.parse(content);
            this.monthlyScheduleCache.put(cacheKey, monthlySchedule);

            return monthlySchedule;

        } catch (MalformedURLException e) {
            throw new SchoolException("교육청 접속 주소가 올바르지 않습니다.");
        }
    }

    public void clearCache() {
        this.monthlyScheduleCache.clear();
        this.monthlyMenuCache.clear();
    }

    private String getContentFromUrl(URL url, String readAfter, String readBefore) throws SchoolException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder buffer = new StringBuilder();
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
