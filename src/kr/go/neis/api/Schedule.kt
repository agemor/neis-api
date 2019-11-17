package kr.go.neis.api

class Schedule {

    var schedule: String = ""

    constructor(schedule: String) {
        this.schedule = schedule
    }

    override fun toString(): String {
        return schedule
    }
}