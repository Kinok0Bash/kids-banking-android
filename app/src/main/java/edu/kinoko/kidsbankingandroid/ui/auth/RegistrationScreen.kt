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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.ui.components.AuthButtonsBlock
import edu.kinoko.kidsbankingandroid.ui.components.FormInput
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun RegistrationScreen(
    home: () -> Unit,
    auth: () -> Unit,
) {
    var step by remember { mutableIntStateOf(1) }

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
                    .height(
                        if (step == 1) {
                            450.dp
                        } else {
                            590.dp
                        }
                    )
                    .background(
                        color = Secondary,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    Header("Регистрация")
                    Spacer(Modifier.size(12.dp))

                    when (step) {
                        1 -> AuthPartForm()
                        2 -> UserInfoPartForm()
                    }
                }

                if (step == 1) {
                    AuthButtonsBlock(
                        buttonText = "Дальше",
                        buttonAction = { step = 2 },
                        textButtonText = "Есть аккаунт? Войти",
                        textButtonAction = auth
                    )
                } else {
                    AuthButtonsBlock(
                        buttonText = "Зарегистрироваться",
                        buttonAction = home,
                        textButtonText = "Назад",
                        textButtonAction = { step = 1 }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthPartForm() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormInput("Логин")
        FormInput("Пароль", isPassword = true)
        FormInput("Повторите пароль", isPassword = true)
    }
}

@Composable
fun UserInfoPartForm() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FormInput("Фамилия")
        FormInput("Имя")
        FormInput("Отчество")
        FormInput("Дата рождения")
        FormInput("Город")
    }
}