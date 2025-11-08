package edu.kinoko.kidsbankingandroid.ui.pay.util

import androidx.camera.core.ImageProxy
import kotlin.math.roundToInt

data class RoiPercents(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    fun asCropRectFor(image: ImageProxy): android.graphics.Rect {
        val w = image.width
        val h = image.height
        val l = (left * w).roundToInt().coerceIn(0, w)
        val t = (top * h).roundToInt().coerceIn(0, h)
        val r = (right * w).roundToInt().coerceIn(l + 1, w)
        val b = (bottom * h).roundToInt().coerceIn(t + 1, h)
        return android.graphics.Rect(l, t, r, b)
    }
}
