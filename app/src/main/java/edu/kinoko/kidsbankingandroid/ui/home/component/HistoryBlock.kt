package edu.kinoko.kidsbankingandroid.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.TransactionHistoryStore
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.grouped

@Composable
fun HistoryBlock() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            when (UserStore.userData.role) {
                Role.PARENT -> "История операций ребенка"
                Role.CHILD -> "История операций"
            },
            style = MaterialTheme.typography.headlineSmall
        )
        TransactionHistoryStore.lastTransactions.forEach {
            Operation(
                name = it.name,
                currency = it.sum.grouped(),
                type = it.category
            )
        }
    }
}