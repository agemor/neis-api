package org.hyunjun.school;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyunJun on 2016-02-27.
 */
public class SchoolScheduleParser {

    /**
     * 웹에서 가져온 데이터를 바탕으로 학사일정을 파싱한다.
     *
     * @param scheduleData
     * @return
     */
    public static List<SchoolSchedule> parse(Document scheduleData) {

        List<SchoolSchedule> monthlySchedule = new ArrayList<SchoolSchedule>();

        Element month = scheduleData.getElementById("contents").getElementsByClass("sub_con")
                .first().getElementsByClass("tbl_calendar").first().getElementsByTag("tbody")
                .first();

        // 월간 일정 목록
        Elements weeks = month.getElementsByTag("tr");
        for (Element week : weeks) {

            // 주간 일정 목록
            Elements days = week.getElementsByTag("td");
            for (Element day : days) {

                // 일간 일정
                Element schedule = day.getElementsByTag("div").first();

                String dateString = schedule.getElementsByTag("em").first().text();
                if (dateString.length() < 1)
                    continue;

                // 일자 취득
                int date = Integer.valueOf(dateString);

                // 일정 취득
                String events = schedule.getElementsByTag("a").text().trim();

                if (events.length() > 1)
                    monthlySchedule.add(new SchoolSchedule(events));
                else
                    monthlySchedule.add(new SchoolSchedule());

            }
        }

        return monthlySchedule;
    }

}
