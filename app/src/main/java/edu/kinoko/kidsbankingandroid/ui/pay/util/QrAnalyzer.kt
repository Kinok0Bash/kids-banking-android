package edu.kinoko.kidsbankingandroid.ui.pay.util

import android.os.SystemClock
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import edu.kinoko.kidsbankingandroid.api.request.PayRequest

class QrAnalyzer(
    private val scanner: BarcodeScanner,
    private val roi: RoiPercents,
    private val onQr: (PayRequest) -> Unit,
    private val requireStableMs: Long = 1_000L
) : ImageAnalysis.Analyzer {

    private var candidate: PayRequest? = null
    private var candidateRaw: String? = null
    private var firstSeenMs: Long = 0L
    private var confirmed = false

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return imageProxy.close()

        imageProxy.setCropRect(roi.asCropRectFor(imageProxy))
        val input = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(input)
            .addOnSuccessListener { list ->
                if (confirmed) return@addOnSuccessListener

                // Берём первый валидный PayRequest среди найденных баркодов
                val parsed: Pair<String, PayRequest>? = list.firstNotNullOfOrNull { bc ->
                    val raw = bc.rawValue
                    val pr = parsePayRequest(raw)
                    if (pr != null) raw!! to pr else null
                }

                val now = SystemClock.elapsedRealtime()
                if (parsed == null) {
                    // ничего валидного не видим — сброс
                    candidate = null
                    candidateRaw = null
                    firstSeenMs = 0L
                    return@addOnSuccessListener
                }

                val (raw, pr) = parsed
                if (candidateRaw == raw) {
                    if (firstSeenMs == 0L) firstSeenMs = now
                    val held = now - firstSeenMs
                    if (held >= requireStableMs) {
                        confirmed = true
                        onQr(pr)
                    }
                } else {
                    candidate = pr
                    candidateRaw = raw
                    firstSeenMs = now
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
