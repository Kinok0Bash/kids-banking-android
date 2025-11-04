package edu.kinoko.kidsbankingandroid.ui.childaccount.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes

@Composable
fun ChildAccountButtonBlock(
    nav: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(0.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ChildAccountMenuButton(
            onClick = {
                nav.navigate(AppRoutes.MONEY_SENDING) {
                    popUpTo(nav.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = Icons.Default.Add,
            text = "Пополнить счет"
        )
        ChildAccountMenuButton(
            icon = Icons.Default.History,
            text = "История операций"
        )
        ChildAccountMenuButton(
            icon = Icons.Default.Lock,
            text = "Ограничить категории"
        )
    }
}