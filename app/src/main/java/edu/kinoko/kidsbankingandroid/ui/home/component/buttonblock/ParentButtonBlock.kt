package edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.store.UserStore
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
            Button(
                onClick = toKidAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Перейти к счету ребенка")
            }
        }
        Button(
            onClick = getSalary,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Получить зарплату")
        }
    }
}