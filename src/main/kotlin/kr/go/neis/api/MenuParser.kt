package kr.go.neis.api

/**
 * NEIS API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 4.0
 */
object MenuParser {

    /**
     * 웹에서 가져온 데이터를 바탕으로 급식 메뉴를 파싱합니다.
     */
    fun parse(rawData: String): List<Menu> {

        if (rawData.isEmpty())
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
                        if (buffer.isNotEmpty())
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
        } catch (e: Exception) {
            throw NEISException("급식 정보 파싱에 실패했습니다. 최신 버전의 API로 업데이트해 주세요.")
        }

        if (monthlyMenu.isEmpty()) {
            throw NEISException("교육기관의 정보가 올바르지 않습니다.")
        }

        return monthlyMenu
    }

    /**
     * 하루의 식단을 파싱합니다.
     */
    private fun parseDay(rawData: String): Menu {

        val menu = Menu()

        // & 표기를 수정합니다.
        var rawData = rawData.replace("&amp", "&")

        val chunk = rawData.split("<br/>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // 0 - 조식, 1 - 중식, 2 - 석식
        var parsingMode = 0
        val menuStrings = arrayOfNulls<StringBuilder>(3)
        for (i in 0..2)
            menuStrings[i] = StringBuilder()

        for (i in 1 until chunk.size) {

            if (chunk[i].trim().isEmpty())
                continue

            var label = false;

            when (chunk[i]) {
                "[조식]" -> {
                    parsingMode = 0
                    label = true
                }
                "[중식]" -> {
                    parsingMode = 1
                    label = true
                }
                "[석식]" -> {
                    parsingMode = 2
                    label = true
                }
            }

            // 메뉴를 해당 시간에 맞게 추가합니다.
            if (!label)
                menuStrings[parsingMode]?.append(chunk[i] + "\n")
        }

        menu.breakfast = menuStrings[0].toString().trim()
        menu.lunch = menuStrings[1].toString().trim()
        menu.dinner = menuStrings[2].toString().trim()

        return menu
    }
}