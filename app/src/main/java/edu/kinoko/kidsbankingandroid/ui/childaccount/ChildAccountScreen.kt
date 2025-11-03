package edu.kinoko.kidsbankingandroid.ui.childaccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.childaccount.component.ChildAccountButtonBlock
import edu.kinoko.kidsbankingandroid.ui.components.AccountCart
import edu.kinoko.kidsbankingandroid.ui.components.BackHeader

@Composable
fun ChildAccountScreen(
    nav: NavHostController
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                BackHeader(
                    text = "Финансовый профиль ребенка",
                    onClick = {
                        nav.navigate(AppRoutes.HOME) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(Modifier.size(12.dp))
                AccountCart(
                    cartName = "Счёт ребёнка",
                    moneyQuantity = BalanceStore.childBalance.grouped()
                )
            }
            ChildAccountButtonBlock()
        }
    }
}