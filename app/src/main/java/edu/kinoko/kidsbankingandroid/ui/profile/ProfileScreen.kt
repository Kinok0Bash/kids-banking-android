package edu.kinoko.kidsbankingandroid.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.components.BackHeader
import edu.kinoko.kidsbankingandroid.ui.components.CustomButton
import edu.kinoko.kidsbankingandroid.ui.profile.component.UserInfo
import edu.kinoko.kidsbankingandroid.ui.splash.SessionViewModel
import edu.kinoko.kidsbankingandroid.ui.theme.ButtonRed

@Composable
fun ProfileScreen(
    nav: NavHostController,
) {
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BackHeader(
                    text = "Личный кабинет",
                    onClick = {
                        nav.navigate(AppRoutes.HOME) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
                UserInfo()
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    text = "Выйти",
                    onClick = {
                        sessionVm.logout()
                        nav.navigate(AppRoutes.AUTH) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    color = ButtonRed
                )
                Text(
                    "uuid: ${UserStore.userData.id}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}