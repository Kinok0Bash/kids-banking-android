package edu.kinoko.kidsbankingandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.enums.TransactionType
import edu.kinoko.kidsbankingandroid.ui.auth.AuthScreen
import edu.kinoko.kidsbankingandroid.ui.auth.RegistrationScreen
import edu.kinoko.kidsbankingandroid.ui.childaccount.ChildAccountScreen
import edu.kinoko.kidsbankingandroid.ui.globalerror.GlobalError
import edu.kinoko.kidsbankingandroid.ui.home.HomeScreen
import edu.kinoko.kidsbankingandroid.ui.moneysending.MoneySendingScreen
import edu.kinoko.kidsbankingandroid.ui.pay.QrPayScreen
import edu.kinoko.kidsbankingandroid.ui.profile.ProfileScreen
import edu.kinoko.kidsbankingandroid.ui.splash.SplashScreen
import edu.kinoko.kidsbankingandroid.ui.splash.TransactionSplashScreen
import edu.kinoko.kidsbankingandroid.ui.theme.KidsBankingAndroidTheme
import edu.kinoko.kidsbankingandroid.ui.transactionresult.TransactionStatusScreen

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

                    composable(route = AppRoutes.GLOBAL_ERROR) {
                        GlobalError(
                            retry = {
                                nav.navigate(AppRoutes.SPLASH) {
                                    popUpTo(nav.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable(route = AppRoutes.HOME) {
                        HomeScreen(nav)
                    }

                    composable(route = AppRoutes.AUTH) {
                        AuthScreen(nav)
                    }

                    composable(route = AppRoutes.REGISTRATION) {
                        RegistrationScreen(
                            nav = nav,
                            regType = Role.PARENT
                        )
                    }

                    composable(route = AppRoutes.NEW_CHILD) {
                        RegistrationScreen(
                            nav = nav,
                            regType = Role.CHILD
                        )
                    }

                    composable(route = AppRoutes.PROFILE) {
                        ProfileScreen(nav)
                    }

                    composable(route = AppRoutes.CHILD_ACCOUNT) {
                        ChildAccountScreen(nav)
                    }

                    composable(route = AppRoutes.MONEY_SENDING) {
                        MoneySendingScreen(nav)
                    }

                    composable(route = AppRoutes.QR_PAY) {
                        QrPayScreen(nav)
                    }

                    composable(
                        route = "${AppRoutes.TRANSACTION_SPLASH}?type={type}&to={to}&sum={sum}",
                        arguments = listOf(
                            navArgument("sum") { type = NavType.IntType },
                            navArgument("to") { type = NavType.IntType },
                            navArgument("type") { type = NavType.StringType },
                        )
                    ) { backStackEntry ->
                        val sum = backStackEntry.arguments?.getInt("sum") ?: 0
                        val type = backStackEntry.arguments?.getString("type") ?: ""
                        val to = backStackEntry.arguments?.getInt("to") ?: 0
                        TransactionSplashScreen(
                            nav = nav,
                            type = TransactionType.parce(type),
                            to = to,
                            sum = sum,
                        )
                    }

                    composable(
                        route = "${AppRoutes.TRANSACTION_STATUS}?status={status}&sum={sum}",
                        arguments = listOf(
                            navArgument("status") { type = NavType.StringType },
                            navArgument("sum") { type = NavType.IntType },
                        )
                    ) { backStackEntry ->
                        TransactionStatusScreen(
                            nav = nav,
                            statusString = backStackEntry.arguments?.getString("status") ?: "FAIL",
                            sum = backStackEntry.arguments?.getInt("sum") ?: -1
                        )
                    }
                }
            }
        }
    }
}
