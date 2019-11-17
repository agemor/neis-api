package kr.go.neis.api

/**
 * 문자열에서 delimiter 전까지의 문자열을 반환합니다.
 */
fun before(string: String, delimiter: String): String {
    val index = string.indexOf(delimiter)
    return string.substring(0, index)
}

/**
 * 문자열에서 delimiter 이후의 문자열을 반환합니다.
 */
fun after(string: String, delimiter: String): String {
    val index = string.indexOf(delimiter)
    return string.substring(index + delimiter.length)
}
