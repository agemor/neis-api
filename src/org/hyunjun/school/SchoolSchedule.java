package org.hyunjun.school;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.0
 */
public class SchoolSchedule {

    public String schedule;

    /**
     * 일정이 없을 경우
     */
    public SchoolSchedule() {
        schedule = "";
    }

    /**
     * 일정이 있을 경우
     *
     * @param schedule
     */
    public SchoolSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return schedule;
    }
}
