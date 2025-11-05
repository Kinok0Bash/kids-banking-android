package edu.kinoko.kidsbankingandroid.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.data.util.grouped
import edu.kinoko.kidsbankingandroid.ui.components.AccountCart
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.home.component.HistoryBlock
import edu.kinoko.kidsbankingandroid.ui.home.component.ProfileButton
import edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock.ChildButtonBlock
import edu.kinoko.kidsbankingandroid.ui.home.component.buttonblock.ParentButtonBlock
import edu.kinoko.kidsbankingandroid.ui.theme.White
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    nav: NavHostController
) {
    val scope = rememberCoroutineScope()
    val vm: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory())
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
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    ProfileButton(
                        name = UserStore.userData.name,
                        onClick = {
                            scope.launch {
                                nav.navigate(route = AppRoutes.PROFILE)
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
                    HistoryBlock(
                        onClick = {
                            nav.navigate(AppRoutes.HISTORY) {
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                when(UserStore.userData.role) {
                     Role.PARENT -> ParentButtonBlock(
                        toKidAccount = {
                            nav.navigate(AppRoutes.CHILD_ACCOUNT) {
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        getSalary = { vm.getSalary() }
                    )

                    Role.CHILD -> ChildButtonBlock(
                        toQrScanner = {
                            nav.navigate(AppRoutes.QR_PAY) {
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }

        if (uiState is UiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize().background(color = White),
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
