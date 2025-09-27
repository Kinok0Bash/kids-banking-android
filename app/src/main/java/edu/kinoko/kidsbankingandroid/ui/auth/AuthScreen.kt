package edu.kinoko.kidsbankingandroid.ui.auth

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.dto.config.FieldConfig
import edu.kinoko.kidsbankingandroid.ui.auth.components.AuthButtonsBlock
import edu.kinoko.kidsbankingandroid.ui.auth.components.DynamicForm
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun AuthScreen(
    home: () -> Unit,
    registration: () -> Unit,
) {
    var formValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val authFields = listOf(
        FieldConfig(AuthFieldNames.LOGIN, "Логин"),
        FieldConfig(AuthFieldNames.PASSWORD, "Пароль", isPassword = true)
    )

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
                    DynamicForm(
                        fields = authFields,
                        values = formValues,
                        onValueChange = { key, value ->
                            formValues = formValues.toMutableMap().apply {
                                this[key] = value
                            }
                        }
                    )
                }
                AuthButtonsBlock(
                    buttonText = "Войти",
                    buttonAction = {
                        Log.d("Auth", formValues.toMap().toString())
                        home()
                    },
                    textButtonText = "Нет аккаунта? Регистрация",
                    textButtonAction = registration,
                )
            }
        }
    }
}
