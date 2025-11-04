package edu.kinoko.kidsbankingandroid.ui.transactionresult

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRuble
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.R
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.enums.TransactionStatus
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.transactionresult.component.ScreenHeader

@Composable
fun TransactionStatusScreen(
    nav: NavHostController,
    statusString: String,
    sum: Int,
) {
    val status = TransactionStatus.parce(statusString)

    val imageSize = 160.dp
    val quantitySize = 36.sp

    val header = when (status) {
        TransactionStatus.OK -> "Успешно"
        TransactionStatus.FAIL, TransactionStatus.FORBIDDEN -> "Что-то пошло не так"
    }

    val text = when (status) {
        TransactionStatus.OK -> "Операция прошла успешно"
        TransactionStatus.FAIL -> "У тебя недостаточно денег на совершение этой покупки. Обратись к родителям, чтобы они отправили тебе еще немного денег"
        TransactionStatus.FORBIDDEN -> "Родители поставили тебе ограничение на покупку. Ты не можешь оплатить покупку, пока они не уберут товар из запрещенных"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ScreenHeader(
                text = header
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                when (status) {
                    TransactionStatus.OK -> {
                        Image(
                            painter = painterResource(R.drawable.success_pay),
                            contentDescription = "",
                            modifier = Modifier.size(imageSize)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "-${sum.grouped()}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontSize = quantitySize,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.CurrencyRuble,
                                contentDescription = "Валюта",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    TransactionStatus.FAIL, TransactionStatus.FORBIDDEN -> {
                        Image(
                            painter = painterResource(R.drawable.declined_pay),
                            contentDescription = "",
                            modifier = Modifier.size(imageSize)
                        )
                        Text(
                            "Отклонено",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = quantitySize,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
                Text(
                    text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(Modifier.size(90.dp))
            }
            CustomButton(
                text = "Хорошо",
                onClick = {
                    when (UserStore.userData.role) {
                        Role.PARENT -> {
                            nav.navigate(AppRoutes.CHILD_ACCOUNT) {
                                popUpTo(nav.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Role.CHILD -> {
                            nav.navigate(AppRoutes.HOME) {
                                popUpTo(nav.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
            )
        }
    }
}