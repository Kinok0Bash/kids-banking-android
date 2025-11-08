package edu.kinoko.kidsbankingandroid.ui.pay.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.delay

private fun Context.vibrator(): Vibrator {
    return if (Build.VERSION.SDK_INT >= 31) {
        getSystemService(VibratorManager::class.java).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}

suspend fun Context.vibrateScanSuccess() {
    val v = vibrator()
    if (!v.hasVibrator()) return

    v.vibrate(
        VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
    )
    delay(30)
    v.vibrate(
        VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
    )
}
