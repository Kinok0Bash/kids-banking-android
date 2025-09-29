package edu.kinoko.kidsbankingandroid.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.dto.FieldConfig
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.ui.auth.components.AuthButtonsBlock
import edu.kinoko.kidsbankingandroid.ui.auth.components.DynamicForm
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validateLogin
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validatePassword
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun AuthScreen(
    home: () -> Unit,
    registration: () -> Unit,
) {
    val vm: AuthViewModel = viewModel(factory = AuthViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()

    var formValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var errors by remember { mutableStateOf<Map<String, String?>>(emptyMap()) }
    var touched by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showAll by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val authFields = listOf(
        FieldConfig(AuthFieldNames.LOGIN, "Логин"),
        FieldConfig(AuthFieldNames.PASSWORD, "Пароль", isPassword = true)
    )

    fun validate(): Map<String, String?> = mapOf(
        AuthFieldNames.LOGIN to validateLogin(formValues[AuthFieldNames.LOGIN].orEmpty()),
        AuthFieldNames.PASSWORD to validatePassword(formValues[AuthFieldNames.PASSWORD].orEmpty())
    )

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) home()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
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
                            errors = errors,
                            touched = if (showAll) {
                                authFields.map { it.key }.toSet()
                            } else {
                                touched
                            },
                            onValueChange = { key, value ->
                                formValues = formValues.toMutableMap().apply { this[key] = value }
                                touched = touched + key
                                errors = validate()
                                vm.resetError()
                            },
                            onLastImeAction = {
                                focusManager.clearFocus(force = true)
                                keyboard?.hide()
                            }
                        )
                    }

                    Spacer(Modifier.size(16.dp))

                    val loading = uiState is AuthUiState.Loading
                    AuthButtonsBlock(
                        buttonText = if (loading) {
                            "Входим..."
                        } else {
                            "Войти"
                        },
                        buttonAction = {
                            showAll = true
                            errors = validate()
                            if (!errors.values.any { it != null } && !loading) {
                                vm.login(formValues)
                            }
                        },
                        textButtonText = "Нет аккаунта? Регистрация",
                        textButtonAction = registration,
                    )
                }
            }
        }

        if (uiState is AuthUiState.Error) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Modal(
                    text = (uiState as AuthUiState.Error).message,
                    modalType = ModalType.ERROR
                )
            }
        }
    }
}
