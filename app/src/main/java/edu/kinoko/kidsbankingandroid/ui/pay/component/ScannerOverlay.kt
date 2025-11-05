package edu.kinoko.kidsbankingandroid.ui.pay.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.pay.util.RoiPercents
import edu.kinoko.kidsbankingandroid.ui.theme.AlfaColor
import edu.kinoko.kidsbankingandroid.ui.theme.White

@Composable
fun ScannerOverlay(roi: RoiPercents) {
    Box(Modifier.fillMaxSize()) {
        Canvas(
            Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            val w = size.width
            val h = size.height
            val rect = Rect(
                offset = Offset(roi.left * w, roi.top * h),
                size = Size((roi.right - roi.left) * w, (roi.bottom - roi.top) * h)
            )

            drawRect(color = Color.Black.copy(alpha = 0.55f))

            drawRoundRect(
                color = AlfaColor,
                topLeft = rect.topLeft,
                size = rect.size,
                cornerRadius = CornerRadius(28f, 28f),
                blendMode = BlendMode.Clear
            )

            val stroke = 8f
            val len = rect.size.minDimension * 0.12f
            fun cornerLines(x: Float, y: Float, dx: Float, dy: Float) {
                drawLine(Color.White, Offset(x, y), Offset(x + dx * len, y), strokeWidth = stroke)
                drawLine(Color.White, Offset(x, y), Offset(x, y + dy * len), strokeWidth = stroke)
            }
            cornerLines(rect.left, rect.top, +1f, +1f)
            cornerLines(rect.right, rect.top, -1f, +1f)
            cornerLines(rect.left, rect.bottom, +1f, -1f)
            cornerLines(rect.right, rect.bottom, -1f, -1f)
        }

        // подсказка
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Наведите камеру на QR-код",
                color = White,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(top = 120.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}