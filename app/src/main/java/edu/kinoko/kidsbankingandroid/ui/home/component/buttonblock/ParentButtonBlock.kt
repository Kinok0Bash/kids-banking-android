package edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.theme.ButtonGreen

@Composable
fun ParentButtonBlock(
    toKidAccount: () -> Unit = {  },
    getSalary: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (UserStore.userData.isGetKid) {
            CustomButton(
                text = "Перейти к счету ребенка",
                onClick = toKidAccount
            )
        }
        CustomButton(
            text = "Получить зарплату",
            onClick = getSalary,
            color = ButtonGreen,
        )
    }
}