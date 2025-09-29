package edu.kinoko.kidsbankingandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.ui.auth.AuthScreen
import edu.kinoko.kidsbankingandroid.ui.auth.RegistrationScreen
import edu.kinoko.kidsbankingandroid.ui.home.HomeScreen
import edu.kinoko.kidsbankingandroid.ui.profile.ProfileScreen
import edu.kinoko.kidsbankingandroid.ui.splash.SplashScreen
import edu.kinoko.kidsbankingandroid.ui.theme.KidsBankingAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsBankingAndroidTheme {
                val nav = rememberNavController()

                val backEntry by nav.currentBackStackEntryAsState()
                val isRoot = nav.previousBackStackEntry == null
                val noExitRoutes = setOf(AppRoutes.AUTH, AppRoutes.HOME) // где не хотим выходить

                if (isRoot && backEntry?.destination?.route in noExitRoutes) {
                    BackHandler(enabled = true) { /* глушим */ }
                }

                NavHost(navController = nav, startDestination = AppRoutes.SPLASH) {

                    composable(route = AppRoutes.SPLASH) {
                        SplashScreen(nav)
                    }

                    composable(route = AppRoutes.HOME) {
                        HomeScreen(nav)
                    }

                    composable(route = AppRoutes.AUTH) {
                        AuthScreen(
                            home = {
                                nav.navigate(AppRoutes.HOME) {
                                    popUpTo(nav.graph.id) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            registration = { nav.navigate(AppRoutes.REGISTRATION) }
                        )
                    }

                    composable(route = AppRoutes.REGISTRATION) {
                        RegistrationScreen(
                            home = {
                                nav.navigate(AppRoutes.HOME) {
                                    popUpTo(nav.graph.id) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            auth = { nav.navigate(AppRoutes.AUTH) },
                        )
                    }

                    composable(route = AppRoutes.PROFILE) {
                        ProfileScreen(nav)
                    }
                }
            }
        }
    }
}
