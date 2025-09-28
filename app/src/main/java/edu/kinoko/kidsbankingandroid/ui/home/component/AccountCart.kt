package edu.kinoko.kidsbankingandroid.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRuble
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.R

@Composable
fun AccountCart(
    cartName: String,
    moneyQuantity: String
) {
    Box(
        modifier = Modifier
            .size(374.dp, 215.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEBD8FF),
                        Color(0x00FFFFFF)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val startFade = size.height * 0.25f
                val brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent
                    ),
                    startY = startFade,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(brush = brush, blendMode = BlendMode.DstIn)
                }
            },
    ) {
        Image(
            painter = painterResource(R.drawable.account_bg),
            contentDescription = null,
            modifier = Modifier
                .alpha(0.2f)
                .fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                cartName,
                modifier = Modifier.height(20.dp),
                autoSize = TextAutoSize.StepBased(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    moneyQuantity,
                    modifier = Modifier.height(24.dp),
                    autoSize = TextAutoSize.StepBased(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Icon(
                    imageVector = Icons.Default.CurrencyRuble,
                    contentDescription = "Валюта",
                    modifier = Modifier
                        .size(18.dp)
                )
            }
        }
    }
}