package edu.kinoko.kidsbankingandroid.ui.limit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.dto.Limit
import edu.kinoko.kidsbankingandroid.ui.limit.CategoryLimitViewModel

@Composable
fun LimitFeed(
    modifier: Modifier = Modifier,
    limits: List<Limit>,
    vm: CategoryLimitViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        limits.forEach {
            LimitCheckBox(it, vm)
        }
    }
}