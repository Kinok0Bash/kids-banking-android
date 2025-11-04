package edu.kinoko.kidsbankingandroid.ui.moneysending.component.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.kinoko.kidsbankingandroid.ui.theme.AlfaColor

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    value: String = " ",
    isDeleteSymbol: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null
) {
    val isEnabled = isDeleteSymbol || value.isNotBlank()
    val interaction = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(70.dp)
            .clip(RoundedCornerShape(0))
            .background(AlfaColor)
            .combinedClickable(
                interactionSource = interaction,
                indication = null,
                enabled = isEnabled,
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isDeleteSymbol) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                contentDescription = "",
                modifier = Modifier.size(26.dp)
            )
        } else {
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium
                ),
            )
        }
    }
}