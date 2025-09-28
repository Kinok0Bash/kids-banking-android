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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.dto.FieldConfig
import edu.kinoko.kidsbankingandroid.ui.auth.components.AuthButtonsBlock
import edu.kinoko.kidsbankingandroid.ui.auth.components.DynamicForm
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun AuthScreen(
    home: () -> Unit,
    registration: () -> Unit,
) {
    val vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AuthViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle() // add: implementation "androidx.lifecycle:lifecycle-runtime-compose:2.8.6"

    var formValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val authFields = listOf(
        FieldConfig(AuthFieldNames.LOGIN, "Логин"),
        FieldConfig(AuthFieldNames.PASSWORD, "Пароль", isPassword = true)
    )

    // навигация на дом после успеха
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) home()
    }

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
                    .background(color = Secondary, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column {
                    Header("Авторизация")
                    Spacer(Modifier.size(12.dp))
                    DynamicForm(
                        fields = authFields,
                        values = formValues,
                        onValueChange = { key, value ->
                            formValues = formValues.toMutableMap().apply { this[key] = value }
                            vm.resetError()
                        }
                    )
                }

                AuthButtonsBlock(
                    buttonText = if (uiState is AuthUiState.Loading) "Входим..." else "Войти",
                    buttonAction = { if (uiState !is AuthUiState.Loading) vm.login(formValues) },
                    textButtonText = "Нет аккаунта? Регистрация",
                    textButtonAction = registration,
                )
            }

            if (uiState is AuthUiState.Error) {
                Spacer(Modifier.size(12.dp))
                androidx.compose.material3.Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}
