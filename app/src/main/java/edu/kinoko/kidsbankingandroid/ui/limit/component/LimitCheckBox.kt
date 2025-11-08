package edu.kinoko.kidsbankingandroid.ui.limit.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.kinoko.kidsbankingandroid.data.dto.Limit
import edu.kinoko.kidsbankingandroid.ui.limit.CategoryLimitViewModel
import edu.kinoko.kidsbankingandroid.ui.theme.AlfaColor

@Composable
fun LimitCheckBox(
    limit: Limit,
    vm: CategoryLimitViewModel
) {
    var isChecked by remember(limit.isLimit) { mutableStateOf(limit.isLimit) }
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    Box(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AlfaColor)
            .combinedClickable(
                interactionSource = interaction,
                indication = LocalIndication.current,
                onClick = {
                    vm.changeLimit(limit.id)
                    isChecked = !isChecked
                }
            )
            .drawWithContent {
                drawContent()
                if (pressed) {
                    drawRect(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.12f))
                }
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    vm.changeLimit(limit.id)
                    isChecked = !isChecked
                }
            )
            Text(
                text = limit.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp
                )
            )
        }
    }
}