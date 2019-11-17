

fun main(args: Array<String>) {

    val school = School.find(School.Region.SEOUL, "선덕고등학교")

    var menu = school.getMonthlyMenu(2019, 1);
    println(menu[1].lunch)

    var schedule = school.getMonthlySchedule(2018, 12)
    println(schedule[4])

    for (i in menu.indices) {
        println("${i + 1}일 메뉴")
        println(menu[i])
    }

}