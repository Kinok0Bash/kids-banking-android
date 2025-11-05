package edu.kinoko.kidsbankingandroid.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.components.ScreenHeader
import edu.kinoko.kidsbankingandroid.ui.history.component.OperationsGroup
import edu.kinoko.kidsbankingandroid.ui.theme.White
import edu.kinoko.kidsbankingandroid.ui.util.UiState

@Composable
fun HistoryScreen(
    nav: NavHostController
) {
    val vm: HistoryScreenViewModel = viewModel(factory = HistoryScreenViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()

    val transactionGroups = vm.transactionGroups.collectAsStateWithLifecycle()

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
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ScreenHeader("История операций")

                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    transactionGroups.value.forEach { (date, transactions) ->
                        OperationsGroup(date, transactions)
                    }
                }

                CustomButton(
                    text = "Назад",
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

        if (uiState is UiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = White),
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