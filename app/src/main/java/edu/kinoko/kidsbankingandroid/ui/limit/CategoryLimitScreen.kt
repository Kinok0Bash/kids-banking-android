package edu.kinoko.kidsbankingandroid.ui.limit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import edu.kinoko.kidsbankingandroid.ui.components.BackHeader
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.limit.component.LimitFeed
import edu.kinoko.kidsbankingandroid.ui.theme.White
import edu.kinoko.kidsbankingandroid.ui.util.UiState

@Composable
fun CategoryLimitScreen(
    nav: NavHostController
) {
    val vm: CategoryLimitViewModel = viewModel(factory = CategoryLimitViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()

    val limits by vm.limits.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.getLimits() }

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
                BackHeader(
                    text = "Запрещенные категории",
                    onClick = {
                        nav.navigate(AppRoutes.CHILD_ACCOUNT) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
                LimitFeed(
                    Modifier.weight(1f).fillMaxSize(),
                    limits,
                    vm
                )
                CustomButton(
                    text = "Сохранить",
                    onClick = {
                        vm.saveLimits()
                        nav.navigate(AppRoutes.CHILD_ACCOUNT) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
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