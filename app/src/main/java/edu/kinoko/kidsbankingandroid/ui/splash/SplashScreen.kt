package edu.kinoko.kidsbankingandroid.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes

@Composable
fun SplashScreen(nav: NavHostController) {
    val sessionVm: SessionViewModel = viewModel(factory = SessionViewModel.factory())

    val state by sessionVm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { sessionVm.bootstrap() }

    LaunchedEffect(state) {
        when (state) {
//            SessionState.Authed -> nav.navigate(AppRoutes.HOME) {
            SessionState.Authed -> nav.navigate(AppRoutes.PROFILE) {
                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                launchSingleTop = true
            }

            SessionState.Guest -> nav.navigate(AppRoutes.AUTH) {
                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                launchSingleTop = true
            }

            SessionState.Checking -> Unit
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
