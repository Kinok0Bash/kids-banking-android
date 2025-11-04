package edu.kinoko.kidsbankingandroid.ui.moneysending.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRuble
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MoneyField(
    value: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                value,
                modifier = Modifier.height(50.dp),
                autoSize = TextAutoSize.StepBased(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Icon(
                imageVector = Icons.Default.CurrencyRuble,
                contentDescription = "Валюта",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}