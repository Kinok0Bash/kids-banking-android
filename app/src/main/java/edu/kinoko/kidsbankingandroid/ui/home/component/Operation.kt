package edu.kinoko.kidsbankingandroid.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import edu.kinoko.kidsbankingandroid.ui.theme.Black
import edu.kinoko.kidsbankingandroid.ui.theme.TransactionGreen

@Composable
fun Operation(
    id: Int,
    name: String,
    currency: String,
    type: String,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (textColor, text) = if (id == 0) {
                    TransactionGreen to "+ $currency"
                } else {
                    Black to "- $currency"
                }
                Text(
                    text,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                )
                Icon(
                    imageVector = Icons.Default.CurrencyRuble,
                    contentDescription = "Валюта",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(14.dp),
                    tint = textColor
                )
            }
        }
        Text(
            type,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}