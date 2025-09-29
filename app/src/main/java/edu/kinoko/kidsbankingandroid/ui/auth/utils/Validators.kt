package edu.kinoko.kidsbankingandroid.ui.auth.utils

import kotlinx.datetime.LocalDate

private val LOGIN_REGEX = Regex("^[A-Za-z0-9]+$")
private val PASSWORD_ALLOWED_REGEX = Regex("^[A-Za-z0-9\\p{Punct}]+$")
private val CYRILLIC_REGEX = Regex("^[А-Яа-яЁё\\s-]+$") // ← теперь пробелы и дефисы

private val easterEggs: List<String> = listOf(
    "Ты нормальный?",
    "Всё в порядке?",
    "Ты серьезно?",
    "Братан, это уже давно не смешно!",
    "Ахахахах. Какой ты оригинальный (нет)",
    "Ээээм... Неееееет...",
)

fun validateLogin(v: String) =
    when {
        v.isBlank() -> "Обязательное поле"
        !LOGIN_REGEX.matches(v) -> "Только латинские буквы и цифры"
        v.lowercase() == "ivanzapara04" -> easterEggs[(0..5).random()]
        else -> null
    }

fun validatePassword(v: String) =
    when {
        v.isBlank() -> "Обязательное поле"
        v.length < 8 -> "Минимум 8 символов"
        !PASSWORD_ALLOWED_REGEX.matches(v) -> "Только латиница, цифры и спец-символы"
        v.lowercase() == "ivanzapara04" -> easterEggs[(0..5).random()]
        else -> null
    }

fun validatePasswordRepeat(p1: String, p2: String) =
    when {
        p2.isBlank() -> "Обязательное поле"
        p1 != p2 -> "Пароли не совпадают"
        p1.lowercase() == "ivanzapara04" -> easterEggs[(0..5).random()]
        p2.lowercase() == "ivanzapara04" -> easterEggs[(0..5).random()]
        else -> null
    }

fun validateCyrillic(v: String) =
    when {
        v.isBlank() -> "Обязательное поле"
        !CYRILLIC_REGEX.matches(v) -> "Только кириллица (разрешены пробел и дефис)"
        v.lowercase() == "ivanzapara04" -> easterEggs[(0..5).random()]
        else -> null
    }

// ===== Дата: храним raw "DDMMYYYY" (8 цифр), показываем как dd.MM.yyyy =====

fun rawToMaskedDdMmYyyy(raw: String): String {
    val d = raw.filter(Char::isDigit).take(8)
    val out = StringBuilder()
    for (i in d.indices) {
        out.append(d[i])
        if (i == 1 || i == 3) out.append('.')
    }
    return out.toString()
}

fun validateBirthDateRaw(raw: String): String? {
    if (raw.isBlank()) return "Обязательное поле"
    if (raw.length != 8) return "Формат: dd.MM.yyyy"
    val dd = raw.substring(0, 2).toIntOrNull()
    val mm = raw.substring(2, 4).toIntOrNull()
    val yy = raw.substring(4, 8).toIntOrNull()
    if (dd == null || mm == null || yy == null) return "Формат: dd.MM.yyyy"
    return runCatching { LocalDate(yy, mm, dd) }.fold(onSuccess = { null }, onFailure = { "Некорректная дата" })
}

fun parseRawDdMmYyyy(raw: String): LocalDate? =
    if (validateBirthDateRaw(raw) == null) {
        val dd = raw.substring(0, 2).toInt()
        val mm = raw.substring(2, 4).toInt()
        val yy = raw.substring(4, 8).toInt()
        LocalDate(yy, mm, dd)
    } else null
