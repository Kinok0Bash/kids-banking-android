package edu.kinoko.kidsbankingandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.kinoko.kidsbankingandroid.ui.home.HomeScreen
import edu.kinoko.kidsbankingandroid.ui.theme.KidsBankingAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsBankingAndroidTheme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "main") {
//                    composable("welcome") { WelcomeScreen { nav.navigate("tasks") } }
//                    composable("tasks") {
//                        TasksScreen(
//                            backToHomePage = { nav.navigate("welcome") },
//                            createTaskScreen = { nav.navigate("create_task") }
//                        )
//                    }
//                    composable("create_task") { CreateTaskScreen { nav.navigate("tasks") } }
                    composable("main") { HomeScreen() }
                }
            }
        }
    }
}