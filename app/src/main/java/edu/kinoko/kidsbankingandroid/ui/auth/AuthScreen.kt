package edu.kinoko.kidsbankingandroid.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.components.FormInput
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun AuthScreen(
    home: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .background(
                        color = Secondary,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    Header("Авторизация")
                    Spacer(Modifier.size(12.dp))
                    AuthForm()
                }
                ButtonsBlock(home)
            }
        }
    }
}

@Composable
fun AuthForm() {
    FormInput("Логин")
    Spacer(Modifier.size(8.dp))
    FormInput("Пароль", true)
}

@Composable
fun ButtonsBlock(home: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = home,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Войти")
        }
        TextButton(
            onClick = home,
            content = { Text("На главный экран") },
        )
    }
}