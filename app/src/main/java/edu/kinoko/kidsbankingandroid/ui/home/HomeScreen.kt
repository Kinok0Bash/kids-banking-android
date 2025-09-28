package edu.kinoko.kidsbankingandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.home.component.AccountCart
import edu.kinoko.kidsbankingandroid.ui.home.component.Operation
import edu.kinoko.kidsbankingandroid.ui.home.component.ProfileButton
import edu.kinoko.kidsbankingandroid.ui.splash.SessionViewModel
import edu.kinoko.kidsbankingandroid.ui.theme.ButtonGreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    nav: NavHostController
) {
    val scope = rememberCoroutineScope()
    val sessionVm: SessionViewModel = viewModel(factory = SessionViewModel.factory())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                ProfileButton(
                    name = UserStore.userData.name,
                    onClick = {
                        scope.launch {
                            sessionVm.logout()
                            nav.navigate(AppRoutes.AUTH) {
                                popUpTo(nav.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
                AccountCart(
                    cartName = if (UserStore.userData.role == Role.PARENT) {
                        "Счёт родителя"
                    } else {
                        "Счёт ребёнка"
                    },
                    moneyQuantity = "1 084"
                )
                Spacer(Modifier.size(10.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        "История операций ребенка",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Operation(
                        name = "Магнит",
                        currency = "112",
                        type = "Покупка в магазине"
                    )
                    Operation(
                        name = "Магнит",
                        currency = "112",
                        type = "Покупка в магазине"
                    )
                    Operation(
                        name = "Магнит",
                        currency = "112",
                        type = "Покупка в магазине"
                    )
                    Operation(
                        name = "Магнит",
                        currency = "112",
                        type = "Покупка в магазине"
                    )
                    Operation(
                        name = "Магнит",
                        currency = "112",
                        type = "Покупка в магазине"
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Перейти к счету ребенка")
                }
                Button(
                    onClick = { },
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
    }
}