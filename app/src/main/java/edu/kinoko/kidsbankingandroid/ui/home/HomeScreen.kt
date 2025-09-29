package edu.kinoko.kidsbankingandroid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.home.component.AccountCart
import edu.kinoko.kidsbankingandroid.ui.home.component.HistoryBlock
import edu.kinoko.kidsbankingandroid.ui.home.component.ProfileButton
import edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock.ParentButtonBlock
import edu.kinoko.kidsbankingandroid.ui.splash.SessionViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    nav: NavHostController
) {
    val scope = rememberCoroutineScope()
    val sessionVm: SessionViewModel = viewModel(factory = SessionViewModel.factory())
    val homeScreenVm: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory())
    val uiState by homeScreenVm.ui.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { homeScreenVm.bootstrap() }

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
                    when (UserStore.userData.role) {
                        Role.PARENT -> {
                            AccountCart(
                                cartName = "Счёт родителя",
                                moneyQuantity = BalanceStore.parentBalance.grouped()
                            )
                        }

                        Role.CHILD -> {
                            AccountCart(
                                cartName = "Счёт ребёнка",
                                moneyQuantity = BalanceStore.childBalance.grouped()
                            )
                        }
                    }
                    Spacer(Modifier.size(10.dp))
                    HistoryBlock()
                }
                when {
                    UserStore.userData.role == Role.PARENT -> ParentButtonBlock(
                        getSalary = { homeScreenVm.getSalary() }
                    )
                }
            }
        }

        if (uiState is HomeScreenUiState.Error) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Modal(
                    text = (uiState as HomeScreenUiState.Error).message,
                    modalType = ModalType.ERROR
                )
            }
        }
    }
}
