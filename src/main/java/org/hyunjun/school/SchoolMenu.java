package org.hyunjun.school;

/**
 * 일간 급식 메뉴
 */
public class SchoolMenu {
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
    public SchoolMenu() {
        breakfast = lunch = dinner = "급식이 없습니다";
    }

    /**
     * 급식이 있는 날
     *
     * @param menu
     */
    public SchoolMenu(String menu) {

        breakfast = lunch = dinner = "급식이 없습니다";

        System.out.println(menu);

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
