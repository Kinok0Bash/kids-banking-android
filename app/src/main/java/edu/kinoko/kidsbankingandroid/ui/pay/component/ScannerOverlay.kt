package edu.kinoko.kidsbankingandroid.ui.pay.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.pay.util.RoiPercents
import edu.kinoko.kidsbankingandroid.ui.theme.White
import kotlin.math.min

@Composable
fun ScannerOverlay(
    roi: RoiPercents,
    // проценты от min(side) выреза
    cutoutRadiusRatio: Float = 0.06f,
    cornerLenRatio: Float = 0.12f,
    cornerArcRatio: Float = 0.06f,
    cornerStroke: Float = 8f
) {
    Box(Modifier.fillMaxSize()) {
        Canvas(
            Modifier
                .fillMaxSize()
                // для BlendMode.Clear нужен offscreen
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            val w = size.width
            val h = size.height
            val rect = Rect(
                offset = Offset(roi.left * w, roi.top * h),
                size = Size((roi.right - roi.left) * w, (roi.bottom - roi.top) * h)
            )

            // затемняем фон
            drawRect(Color.Black.copy(alpha = 0.55f))

            // окно сканирования с настраиваемым радиусом
            val cutoutR = cutoutRadiusRatio * min(rect.width, rect.height)
            drawRoundRect(
                color = Color.Transparent,
                topLeft = rect.topLeft,
                size = rect.size,
                cornerRadius = CornerRadius(cutoutR, cutoutR),
                blendMode = BlendMode.Clear
            )

            // уголки: длина и радиус дуги в процентах
            val minSide = min(rect.width, rect.height)
            val len = (cornerLenRatio * minSide).coerceAtLeast(1f)
            val rArc = (cornerArcRatio * minSide).coerceIn(0f, len - 1f)

            val stroke = Stroke(
                width = cornerStroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            // вспомогалки
            fun drawTopLeft() {
                val path = Path().apply {
                    // горизонталь (справа налево)
                    moveTo(rect.left + len, rect.top)
                    lineTo(rect.left + rArc, rect.top)
                    // дуга 90° вокруг центра (left+rArc, top+rArc), из 270° к 180° (CCW)
                    arcTo(
                        Rect(rect.left, rect.top, rect.left + 2 * rArc, rect.top + 2 * rArc),
                        startAngleDegrees = 270f,
                        sweepAngleDegrees = -90f,
                        forceMoveTo = false
                    )
                    // вертикаль (сверху вниз)
                    lineTo(rect.left, rect.top + len)
                }
                drawPath(path, Color.White, style = stroke)
            }

            fun drawTopRight() {
                val path = Path().apply {
                    moveTo(rect.right - len, rect.top)
                    lineTo(rect.right - rArc, rect.top)
                    arcTo(
                        Rect(rect.right - 2 * rArc, rect.top, rect.right, rect.top + 2 * rArc),
                        startAngleDegrees = 270f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    lineTo(rect.right, rect.top + len)
                }
                drawPath(path, Color.White, style = stroke)
            }

            fun drawBottomLeft() {
                val path = Path().apply {
                    moveTo(rect.left + len, rect.bottom)
                    lineTo(rect.left + rArc, rect.bottom)
                    arcTo(
                        Rect(rect.left, rect.bottom - 2 * rArc, rect.left + 2 * rArc, rect.bottom),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    lineTo(rect.left, rect.bottom - len)
                }
                drawPath(path, Color.White, style = stroke)
            }

            fun drawBottomRight() {
                val path = Path().apply {
                    moveTo(rect.right - len, rect.bottom)
                    lineTo(rect.right - rArc, rect.bottom)
                    arcTo(
                        Rect(
                            rect.right - 2 * rArc,
                            rect.bottom - 2 * rArc,
                            rect.right,
                            rect.bottom
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = -90f,
                        forceMoveTo = false
                    )
                    lineTo(rect.right, rect.bottom - len)
                }
                drawPath(path, Color.White, style = stroke)
            }

            drawTopLeft()
            drawTopRight()
            drawBottomLeft()
            drawBottomRight()
        }

        // подсказка
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Наведите камеру на QR-код",
                color = White,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(top = 120.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
