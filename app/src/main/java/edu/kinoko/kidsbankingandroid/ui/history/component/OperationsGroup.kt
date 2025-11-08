package edu.kinoko.kidsbankingandroid.ui.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.kinoko.kidsbankingandroid.data.dto.Transaction
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.data.util.toHumanString
import edu.kinoko.kidsbankingandroid.ui.components.Operation
import kotlinx.datetime.LocalDate

@Composable
fun OperationsGroup(
    date: LocalDate,
    transactions: List<Transaction>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            date.toHumanString(false),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 30.sp
            )
        )
        transactions.forEach {
            Operation(
                id = it.toId,
                name = it.name,
                currency = it.sum.grouped(),
                type = it.category
            )
        }
    }
}