package edu.kinoko.kidsbankingandroid.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.TransactionHistoryStore
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.components.Operation
import edu.kinoko.kidsbankingandroid.ui.theme.AlfaColor

@Composable
fun HistoryBlock(
    onClick: () -> Unit = {}
) {
    val interaction = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            Modifier
                .height(32.dp)
                .fillMaxWidth(),
        ) {
            when (UserStore.userData.role) {
                Role.PARENT -> {
                    Text(
                        "История операций ребенка",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Role.CHILD -> {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .background(AlfaColor)
                            .clip(RoundedCornerShape(50))
                            .combinedClickable(
                                interactionSource = interaction,
                                onClick = onClick,
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "История операций",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = "",
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            }

        }
        TransactionHistoryStore.lastTransactions.forEach {
            Operation(
                id = it.toId,
                name = it.name,
                currency = it.sum.grouped(),
                type = it.category
            )
        }
    }
}