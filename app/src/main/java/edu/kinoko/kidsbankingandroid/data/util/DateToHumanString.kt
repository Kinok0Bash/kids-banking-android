package edu.kinoko.kidsbankingandroid.data.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate as KotlinLocalDate

private val RU_MONTHS = arrayOf(
    "января", "февраля", "марта", "апреля", "мая", "июня",
    "июля", "августа", "сентября", "октября", "ноября", "декабря"
)

fun KotlinLocalDate.toHumanString(): String = "$day ${RU_MONTHS[month.number - 1]} $year"

@OptIn(ExperimentalTime::class)
fun KotlinLocalDate.toAgeString(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var age = today.year - year
    val hadBirthday = when {
        today.month.number > month.number -> true
        today.month.number < month.number -> false
        else -> today.day >= day
    }
    if (!hadBirthday) age--

    val n = abs(age)
    val word = when {
        n % 100 in 11..14 -> "лет"
        n % 10 == 1 -> "год"
        n % 10 in 2..4 -> "года"
        else -> "лет"
    }
    return "$age $word"
}
