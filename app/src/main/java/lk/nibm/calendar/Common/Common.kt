package lk.nibm.calendar.Common

object Common {

    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "January"
        }
    }

    fun getMonthNumber(month: String): String {
        return when (month) {
            "January" -> "1"
            "February" -> "2"
            "March" -> "3"
            "April" -> "4"
            "May" -> "5"
            "June" -> "6"
            "July" -> "7"
            "August" -> "8"
            "September" -> "9"
            "October" -> "10"
            "November" -> "11"
            "December" -> "12"
            else -> "0"
        }
    }

    fun getDateName(toInt: Int): String {
        return when (toInt) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            4 -> "th"
            5 -> "th"
            6 -> "th"
            7 -> "th"
            8 -> "th"
            9 -> "th"
            10 -> "th"
            11 -> "th"
            12 -> "th"
            13 -> "th"
            14 -> "th"
            15 -> "th"
            16 -> "th"
            17 -> "th"
            18 -> "th"
            19 -> "th"
            20 -> "th"
            21 -> "st"
            22 -> "nd"
            23 -> "rd"
            24 -> "th"
            25 -> "th"
            26 -> "th"
            27 -> "th"
            28 -> "th"
            29 -> "th"
            30 -> "th"
            31 -> "st"
            else -> ""
        }
    }

}