package kr.go.neis.api

object MenuParser {

    /**
     * 웹에서 가져온 데이터를 바탕으로 급식 메뉴를 파싱합니다.
     */
    fun parse(rawData: String): List<Menu> {

        if (rawData.length < 1)
            throw NEISException("불러온 데이터가 올바르지 않습니다.")

        val monthlyMenu = ArrayList<Menu>()

        /*
         파싱 편의를 위해 모든 공백을 제거합니다.
         급식 메뉴의 이름에는 공백이 들어가지 않으므로, 파싱 결과에는 영향을 주지 않습니다.
         */
        val rawData = rawData.replace("\\s+".toRegex(), "")

        /*
         <div> - </div> 쌍을 찾아 그 사이의 데이터를 추출합니다.
         */
        val buffer = StringBuilder()

        var inDiv = false

        try {
            var i = 0
            while (i < rawData.length) {
                if (rawData[i] == 'v') {
                    if (inDiv) {
                        buffer.delete(buffer.length - 4, buffer.length)
                        if (buffer.length > 0)
                            monthlyMenu.add(parseDay(buffer.toString()))
                        buffer.clear()
                    } else {
                        i++
                    }
                    inDiv = !inDiv
                } else if (inDiv) {
                    buffer.append(rawData[i])
                }
                i++
            }

            return monthlyMenu

        } catch (e: Exception) {
            throw NEISException("급식 정보 파싱에 실패했습니다. 최신 버전의 API로 업데이트해 주세요.")
        }
    }

    /**
     * 하루의 식단을 파싱합니다.
     */
    private fun parseDay(rawData: String): Menu {

        val menu = Menu()

        // 의미 없는 단어를 제거합니다.
        var rawData = rawData.replace("(석식)", "")
        rawData = rawData.replace("(선)", "")

        val chunk = rawData.split("<br/>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in 1 until chunk.size) {

            if (chunk[i].trim().isEmpty())
                continue

            when (chunk[i]) {
                "[조식]" -> {
                    menu.breakfast += "\n${chunk[i]}"
                }
                "[중식]" -> {
                    menu.lunch += "\n${chunk[i]}"
                }
                "[석식]" -> {
                    menu.dinner += "\n${chunk[i]}"
                }
            }
        }
        return menu
    }
}