import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 학교 정보 로더
 * 
 * @author HyunJun
 *
 */
public class SchoolAPI {

	/**
	 * 학교 종류
	 * 
	 * @author HyunJun
	 *
	 */
	public static class SchoolType {
		public static final int KINDERGARTEN = 1;
		public static final int ELEMENTARY = 2;
		public static final int MIDDLE = 3;
		public static final int HIGH = 4;
	}

	/**
	 * 지역 관할 교육청 코드
	 * 
	 * @author HyunJun
	 *
	 */
	public static class Country {
		public static final String SEOUL = "hes.sen.go.kr";
		public static final String ULSAN = "hes.use.go.kr";
		public static final String JEONBUK = "hes.jbe.go.kr";
		public static final String BUSAN = "hes.pen.go.kr";
		public static final String SEJONG = "hes.sje.go.kr";
		public static final String JEONNAM = "hes.jne.go.kr";
		public static final String DAEGU = "hes.dge.go.kr";
		public static final String GYEONGGI = "hes.goe.go.kr";
		public static final String GYEONGBUK = "hes.gbe.go.kr";
		public static final String INCHEON = "hes.ice.go.kr";
		public static final String KANGWON = "hes.kwe.go.kr";
		public static final String GYEONGNAM = "hes.gne.go.kr";
		public static final String GWANGJU = "hes.gen.go.kr";
		public static final String CHUNGBUK = "hes.cbe.go.kr";
		public static final String JEJU = "hes.jje.go.kr";
		public static final String DAEJEON = "hes.dje.go.kr";
		public static final String CHUNGNAM = "hes.cne.go.kr";
	}

	/**
	 * 급식 식단표 정보(월간) URL
	 */
	public static final String MENU_URL = "sts_sci_md00_001.do";

	/**
	 * 학사일정 정보(월간) URL
	 */
	public static final String SCHEDULE_URL = "sts_sci_sf01_001.do";

	/**
	 * 월간 급식 식단표를 가져온다.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static MenuData[] getMonthlyMenu(String countryCode, String schoolCode, int schoolType,
			int year, int month) {

		StringBuffer targetURL = new StringBuffer("http://");
		targetURL.append(countryCode);
		targetURL.append("/");
		targetURL.append(MENU_URL);

		try {
			Document menuData = Jsoup.connect(targetURL.toString()).data("schulCode", schoolCode)
					.data("schulCrseScCode", schoolType + "")
					.data("schulKndScCode", "0" + schoolType)
					.data("schYm", year + "." + asTwoWord(month)).post();

			return MenuDataParser.parse(menuData);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 월간 학사 일정을 가져온다.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static ScheduleData[] getMonthlySchedule(String countryCode, String schoolCode,
			int schoolType, int year, int month) {

		StringBuffer targetURL = new StringBuffer("http://");
		targetURL.append(countryCode);
		targetURL.append("/");
		targetURL.append(SCHEDULE_URL);

		try {
			Document scheduleData = Jsoup.connect(targetURL.toString())
					.data("schulCode", schoolCode).data("schulCrseScCode", schoolType + "")
					.data("schulKndScCode", "0" + schoolType).data("ay", "" + year)
					.data("mm", asTwoWord(month)).post();

			return ScheduleDataParser.parse(scheduleData);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static String asTwoWord(int number) {
		if (number >= 10) {
			return "" + number;
		} else {
			return "0" + number;
		}
	}
}

/**
 * 급식 식단표 데이터 파서
 * 
 * @author HyunJun
 *
 */
class MenuDataParser {

	/**
	 * 웹에서 가져온 정보를 바탕으로 급식 식단표를 파싱한다.
	 * 
	 * @param menuData
	 * @return
	 */
	public static MenuData[] parse(Document menuData) {

		MenuData[] monthlyMenu = new MenuData[31];

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

					// 급식 데이터의 첫 단위는 날짜이다.
					String[] chunk = data.split(" ");
					int date = Integer.valueOf(chunk[0]);

					// 급식 정보가 존재할 경우에만 데이터를 쓴다.
					if (chunk.length > 1)
						monthlyMenu[date - 1] = new MenuData(data);
					else
						monthlyMenu[date - 1] = new MenuData();
				}
			}
		}

		return monthlyMenu;
	}
}

/**
 * 학사일정 데이터 파서
 * 
 * @author HyunJun
 *
 */
class ScheduleDataParser {

	/**
	 * 웹에서 가져온 데이터를 바탕으로 학사일정을 파싱한다.
	 * 
	 * @param scheduleData
	 * @return
	 */
	public static ScheduleData[] parse(Document scheduleData) {

		ScheduleData[] monthlySchedule = new ScheduleData[31];

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
					monthlySchedule[date - 1] = new ScheduleData(events);
				else
					monthlySchedule[date - 1] = new ScheduleData();

			}
		}

		return monthlySchedule;
	}
}

/**
 * 하루 식단표
 * 
 * @author HyunJun
 *
 */
class MenuData {
	/**
	 * 조식
	 */
	public String breakfast;

	/**
	 * 중식
	 */
	public String lunch;

	/**
	 * 석식
	 */
	public String dinner;

	/**
	 * 급식이 없는 날
	 */
	public MenuData() {
		breakfast = lunch = dinner = "급식이 없습니다";
	}

	/**
	 * 급식이 있는 날
	 * 
	 * @param menu
	 */
	public MenuData(String menu) {

		breakfast = lunch = dinner = "급식이 없습니다";

		// 간단한 파싱을 수행한다.
		String[] chunk = menu.split(" ");

		// 0 - 조식, 1 - 중식, 2 - 석식
		int parsingMode = 0;

		for (int i = 1; i < chunk.length; i++) {

			if (chunk[i].trim().length() < 1)
				continue;

			if (chunk[i].equals("[조식]")) {
				parsingMode = 0;
				breakfast = "";
				continue;
			} else if (chunk[i].equals("[중식]")) {
				parsingMode = 1;
				lunch = "";
				continue;
			} else if (chunk[i].equals("[석식]")) {
				parsingMode = 2;
				dinner = "";
				continue;
			}

			switch (parsingMode) {
			case 0:
				if (breakfast.length() > 1)
					breakfast += "\n" + chunk[i];
				else
					breakfast += chunk[i];
				break;
			case 1:
				if (lunch.length() > 1)
					lunch += "\n" + chunk[i];
				else
					lunch += chunk[i];
				break;
			case 2:
				if (dinner.length() > 1)
					dinner += "\n" + chunk[i];
				else
					dinner += chunk[i];
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "[아침]\n" + breakfast + "\n" + "[점심]\n" + lunch + "\n" + "[저녁]\n" + dinner;
	}
}

/**
 * 일일 학사일정
 * 
 * @author HyunJun
 *
 */
class ScheduleData {
	public String schedule;

	/**
	 * 일정이 없을 경우
	 */
	public ScheduleData() {
		schedule = "";
	}

	/**
	 * 일정이 있을 경우
	 * 
	 * @param schedule
	 */
	public ScheduleData(String schedule) {
		this.schedule = schedule;
	}
}
