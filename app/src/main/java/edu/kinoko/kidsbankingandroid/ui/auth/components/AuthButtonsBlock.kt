package edu.kinoko.kidsbankingandroid.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton

@Composable
fun AuthButtonsBlock(
    buttonText: String,
    buttonAction: () -> Unit,
    textButtonText: String,
    textButtonAction: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            text = buttonText,
            onClick = buttonAction,
        )
        TextButton(
            onClick = textButtonAction,
            content = {
                Text(textButtonText)
            },
        )
    }
}