package edu.kinoko.kidsbankingandroid.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.api.request.PayRequest
import edu.kinoko.kidsbankingandroid.data.enums.TransactionType
import edu.kinoko.kidsbankingandroid.ui.splash.vm.TransactionSplashViewModel

@Composable
fun TransactionSplashScreen(
    nav: NavHostController,
    type: TransactionType,
    to: Int,
    sum: Int,
) {
    val vm: TransactionSplashViewModel = viewModel(factory = TransactionSplashViewModel.factory())

    LaunchedEffect(type) {
        when (type) {
            TransactionType.QR_PAY -> {
                val route = vm.pay(
                    PayRequest(
                        shopId = to,
                        sum = sum
                    )
                )
                nav.navigate(route) {
                    popUpTo(nav.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
            TransactionType.TRANSFER -> {
                val route = vm.transfer(sum)
                nav.navigate(route) {
                    popUpTo(nav.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(90.dp),
                strokeWidth = 6.dp,
            )
        }
    }
}