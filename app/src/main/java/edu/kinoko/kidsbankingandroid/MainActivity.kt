package edu.kinoko.kidsbankingandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.kinoko.kidsbankingandroid.ui.auth.AuthScreen
import edu.kinoko.kidsbankingandroid.ui.home.HomeScreen
import edu.kinoko.kidsbankingandroid.ui.theme.KidsBankingAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsBankingAndroidTheme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "main") {

                    composable("main") {
                        HomeScreen(
                            auth = { nav.navigate("auth") }
                        )
                    }

                    composable(route = "auth") { AuthScreen() }
                }
            }
        }
    }
}