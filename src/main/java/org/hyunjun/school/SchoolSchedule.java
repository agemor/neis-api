package org.hyunjun.school;

/**
 * 일간 학사 일정
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
