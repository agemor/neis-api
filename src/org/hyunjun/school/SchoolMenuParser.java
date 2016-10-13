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
public class SchoolMenuParser {

    /**
     * 웹에서 가져온 데이터를 바탕으로 급식 메뉴를 파싱합니다.
     *
     * @param rawData
     * @return
     */
    public static List<SchoolMenu> parse(String rawData) throws SchoolException {

        if (rawData.length() < 1)
            throw new SchoolException("불러온 데이터가 올바르지 않습니다.");

        List<SchoolMenu> monthlyMenu = new ArrayList<SchoolMenu>();

        /*
         파싱 편의를 위해 모든 공백을 제거합니다.
         급식 메뉴의 이름에는 공백이 들어가지 않으므로, 파싱 결과에는 영향을 주지 않습니다.
         */
        rawData = rawData.replaceAll("\\s+", "");

        /*
         <div> - </div> 쌍을 찾아 그 사이의 데이터를 추출합니다.
         */
        StringBuffer buffer = new StringBuffer();

        boolean inDiv = false;

        try {
            for (int i = 0; i < rawData.length(); i++) {
                if (rawData.charAt(i) == 'v') {
                    if (inDiv) {
                        buffer.delete(buffer.length() - 4, buffer.length());
                        if (buffer.length() > 0)
                            monthlyMenu.add(parseDay(buffer.toString()));
                        buffer.setLength(0);
                    } else {
                        i++;
                    }
                    inDiv = !inDiv;
                } else if (inDiv) {
                    buffer.append(rawData.charAt(i));
                }
            }

            return monthlyMenu;

        } catch (Exception e) {
            throw new SchoolException("급식 정보 파싱에 실패했습니다. 최신 버전으로 업데이트 해 주세요.");
        }
    }

    private static SchoolMenu parseDay(String rawData) {

        SchoolMenu menu = new SchoolMenu();

        // 의미 없는 단어를 제거합니다.
        rawData = rawData.replace("(석식)", "");
        rawData = rawData.replace("(선)", "");

        String[] chunk = rawData.split("<br/>");

        // 0 - 조식, 1 - 중식, 2 - 석식
        int parsingMode = 0;

        for (int i = 1; i < chunk.length; i++) {

            if (chunk[i].trim().length() < 1)
                continue;

            if (chunk[i].equals("[조식]")) {
                parsingMode = 0;
                menu.breakfast = "";
                continue;
            } else if (chunk[i].equals("[중식]")) {
                parsingMode = 1;
                menu.lunch = "";
                continue;
            } else if (chunk[i].equals("[석식]")) {
                parsingMode = 2;
                menu.dinner = "";
                continue;
            }

            switch (parsingMode) {
                case 0:
                    if (menu.breakfast.length() > 1)
                        menu.breakfast += "\n" + chunk[i];
                    else
                        menu.breakfast += chunk[i];
                    break;
                case 1:
                    if (menu.lunch.length() > 1)
                        menu.lunch += "\n" + chunk[i];
                    else
                        menu.lunch += chunk[i];
                    break;
                case 2:
                    if (menu.dinner.length() > 1)
                        menu.dinner += "\n" + chunk[i];
                    else
                        menu.dinner += chunk[i];
                    break;
            }
        }
        return menu;
    }
}
