package edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton

@Composable
fun ChildButtonBlock(
    toQrScanner: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CustomButton(
            text = "Оплата",
            onClick = toQrScanner,
        )
    }
}