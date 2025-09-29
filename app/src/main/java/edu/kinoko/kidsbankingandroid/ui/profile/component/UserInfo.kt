package edu.kinoko.kidsbankingandroid.ui.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.toAgeString
import edu.kinoko.kidsbankingandroid.data.util.toHumanString

@Composable
fun UserInfo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            UserStore.userData.fullName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        UserInfoRow(
            "Логин",
            UserStore.userData.username
        )
        UserInfoRow(
            "Дата рождения",
            UserStore.userData.birthDate.toHumanString()
        )
        UserInfoRow(
            "Возраст",
            UserStore.userData.birthDate.toAgeString()
        )
        UserInfoRow(
            "Город",
            UserStore.userData.city
        )
        UserInfoRow(
            "Тип аккаунта",
            when (UserStore.userData.role) {
                Role.PARENT -> "Родитель"
                Role.CHILD -> "Ребенок"
            }
        )
    }
}