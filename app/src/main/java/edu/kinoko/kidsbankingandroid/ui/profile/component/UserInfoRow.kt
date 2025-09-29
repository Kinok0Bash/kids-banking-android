package edu.kinoko.kidsbankingandroid.ui.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun UserInfoRow(
    header: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "$header:",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 20.sp
            )
        )
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 20.sp
            )
        )
    }
}