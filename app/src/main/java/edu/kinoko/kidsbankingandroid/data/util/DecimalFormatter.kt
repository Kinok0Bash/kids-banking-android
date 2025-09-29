package edu.kinoko.kidsbankingandroid.data.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private fun decimalFormatWith(space: Char = '\u202F') = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale.ROOT).apply {
        groupingSeparator = space
    }
)

fun Int.grouped(space: Char = '\u202F'): String =
    decimalFormatWith(space).format(this)