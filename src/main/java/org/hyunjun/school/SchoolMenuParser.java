package org.hyunjun.school;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyunJun on 2016-02-27.
 */
public class SchoolMenuParser {

    public static List<SchoolMenu> parse(Document menuData) {

        List<SchoolMenu> monthlyMenu = new ArrayList<SchoolMenu>();

        Element month = menuData.getElementById("contents").getElementsByClass("sub_con").first()
                .getElementsByClass("tbl_calendar").first().getElementsByTag("tbody").first();

        // 월 단위 메뉴 목록
        Elements weeks = month.getElementsByTag("tr");
        for (Element week : weeks) {

            // 주 단위 메뉴 목록
            Elements days = week.getElementsByTag("td");
            for (Element day : days) {

                // 일 단위 메뉴 목록
                Elements menus = day.getElementsByTag("div").first().getAllElements();
                for (Element menu : menus) {

                    // 빈 메뉴가 아닐 경우에만 프로세싱
                    String data = menu.text().trim();
                    if (data.length() < 1)
                        continue;

                    // 불필요한 문자를 제거한다.
                    data = data.replace("(석식)", "");
                    data = data.replace("(선)", "");

                    // 급식 데이터의 첫 단위는 날짜이다.
                    String[] chunk = data.split(" ");
                    int date = Integer.valueOf(chunk[0]);

                    // 급식 정보가 존재할 경우에만 데이터를 쓴다.
                    if (chunk.length > 1)
                        monthlyMenu.add(new SchoolMenu(data));
                    else
                        monthlyMenu.add(new SchoolMenu());
                }
            }
        }

        return monthlyMenu;
    }
}
