package org.hyunjun.school;

import java.util.ArrayList;
import java.util.List;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.0
 */
public class SchoolScheduleParser {

    /**
     * 웹에서 가져온 데이터를 바탕으로 학사일정을 파싱합니다.
     *
     * @param rawData
     * @return
     */
    public static List<SchoolSchedule> parse(String rawData) throws SchoolException {

        if (rawData.length() < 1)
            throw new SchoolException("불러온 데이터가 올바르지 않습니다.");

        List<SchoolSchedule> monthlySchedule = new ArrayList<SchoolSchedule>();

        /*
         파싱 편의를 위해 모든 공백을 제거합니다.
         일정 텍스트에는 공백이 들어가지 않으므로, 파싱 결과에는 영향을 주지 않습니다.
         */
        rawData = rawData.replaceAll("\\s+", "");

        String[] chunk = rawData.split("textL\">");

        try {
            for (int i = 1; i < chunk.length; i++) {
                String trimmed = before(chunk[i], "</div>");
                String date = before(after(trimmed, ">"), "</em>");

                // 빈 공간은 파싱하지 않습니다.
                if (date.length() < 1) continue;

                // 일정이 있는 경우
                if (trimmed.indexOf("<strong>") > 0) {
                    String name = before(after(trimmed, "<strong>"), "</strong>");
                    monthlySchedule.add(new SchoolSchedule(name));
                }
                // 일정이 없는 경우
                else {
                    monthlySchedule.add(new SchoolSchedule());
                }
            }
            return monthlySchedule;

        } catch (Exception e) {
            throw new SchoolException("학사일정 정보 파싱에 실패했습니다. 최신 버전으로 업데이트 해 주세요.");
        }
    }

    /**
     * 문자열에서 delimiter 전까지의 문자열을 반환합니다.
     *
     * @param string
     * @param delimiter
     * @return
     */
    private static String before(String string, String delimiter) {
        int index = string.indexOf(delimiter);
        return string.substring(0, index);
    }

    /**
     * 문자열에서 delimiter 이후의 문자열을 반환합니다.
     *
     * @param string
     * @param delimiter
     * @return
     */
    private static String after(String string, String delimiter) {
        int index = string.indexOf(delimiter);
        return string.substring(index + delimiter.length(), string.length());
    }

}
