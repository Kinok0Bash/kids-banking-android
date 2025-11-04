package edu.kinoko.kidsbankingandroid.ui.moneysending

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
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.components.BackHeader
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.moneysending.component.MoneyField
import edu.kinoko.kidsbankingandroid.ui.moneysending.component.UserActionBlock
import edu.kinoko.kidsbankingandroid.ui.util.UiState

@Composable
fun MoneySendingScreen(
    nav: NavHostController
) {
    val vm: MoneySendingViewModel = viewModel(factory = MoneySendingViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()
    val amount by vm.amount.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) { vm.bootstrap() }

    Box(modifier = Modifier.fillMaxSize()) {
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
                BackHeader(
                    text = "Перевод ребенку",
                    onClick = {
                        nav.navigate(AppRoutes.CHILD_ACCOUNT) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
                MoneyField(amount.grouped())
                UserActionBlock(vm, nav)
            }
        }

        if (uiState is UiState.Error) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
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
