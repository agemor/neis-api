package kr.go.neis.api

class Menu {

    var breakfast: String = "급식이 없습니다"
    var lunch: String = "급식이 없습니다"
    var dinner: String = "급식이 없습니다"

    override fun toString(): String {
        return "[아침]\n$breakfast\n[점심]\n$lunch\n[저녁]\n$dinner"
    }
}