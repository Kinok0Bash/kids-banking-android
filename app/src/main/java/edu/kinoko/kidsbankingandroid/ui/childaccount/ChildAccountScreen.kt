package edu.kinoko.kidsbankingandroid.ui.childaccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.childaccount.component.ChildAccountButtonBlock
import edu.kinoko.kidsbankingandroid.ui.components.AccountCart
import edu.kinoko.kidsbankingandroid.ui.components.BackHeader
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.util.UiState

@Composable
fun ChildAccountScreen(
    nav: NavHostController
) {
    val vm: ChildAccountViewModel = viewModel(factory = ChildAccountViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.bootstrap() }

    Box(modifier = Modifier.fillMaxSize()) {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BackHeader(
                        text = "Финансовый профиль ребенка",
                        onClick = {
                            nav.navigate(AppRoutes.HOME) {
                                popUpTo(nav.graph.id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    AccountCart(
                        cartName = "Счёт ребёнка",
                        moneyQuantity = BalanceStore.childBalance.grouped()
                    )
                }
                ChildAccountButtonBlock(nav)
            }
        }

        if (uiState is UiState.Error) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Modal(
                    text = (uiState as UiState.Error).message,
                    modalType = ModalType.ERROR
                )
            }
        }
    }
}