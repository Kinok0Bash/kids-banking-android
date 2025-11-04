package edu.kinoko.kidsbankingandroid.ui.moneysending.component.keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SendingKeyboard(
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyboardButton(Modifier.weight(1f), value = "1", onClick = { onDigit(1) })
            KeyboardButton(Modifier.weight(1f), value = "2", onClick = { onDigit(2) })
            KeyboardButton(Modifier.weight(1f), value = "3", onClick = { onDigit(3) })
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyboardButton(Modifier.weight(1f), value = "4", onClick = { onDigit(4) })
            KeyboardButton(Modifier.weight(1f), value = "5", onClick = { onDigit(5) })
            KeyboardButton(Modifier.weight(1f), value = "6", onClick = { onDigit(6) })
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyboardButton(Modifier.weight(1f), value = "7", onClick = { onDigit(7) })
            KeyboardButton(Modifier.weight(1f), value = "8", onClick = { onDigit(8) })
            KeyboardButton(Modifier.weight(1f), value = "9", onClick = { onDigit(9) })
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyboardButton(Modifier.weight(1f))
            KeyboardButton(Modifier.weight(1f), value = "0", onClick = { onDigit(0) })
            KeyboardButton(Modifier.weight(1f), isDeleteSymbol = true, onClick = onBackspace, onLongClick = onClear)
        }
    }
}