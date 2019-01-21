package kr.go.neis.api;

/**
 * NEIS API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.1
 */
public class Utils {

    /**
     * 문자열에서 delimiter 전까지의 문자열을 반환합니다.
     */
    public static String before(String string, String delimiter) {
        int index = string.indexOf(delimiter);
        return string.substring(0, index);
    }

    /**
     * 문자열에서 delimiter 이후의 문자열을 반환합니다.
     */
    public static String after(String string, String delimiter) {
        int index = string.indexOf(delimiter);
        return string.substring(index + delimiter.length());
    }
}
