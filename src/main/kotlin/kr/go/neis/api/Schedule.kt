package kr.go.neis.api

/**
 * NEIS API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 4.0
 */
class Schedule {

    var schedule: String = ""

    constructor(schedule: String) {
        this.schedule = schedule
    }

    override fun toString(): String {
        return schedule
    }
}