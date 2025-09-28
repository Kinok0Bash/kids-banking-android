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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.dto.FieldConfig
import edu.kinoko.kidsbankingandroid.data.enums.ModalType
import edu.kinoko.kidsbankingandroid.ui.auth.components.AuthButtonsBlock
import edu.kinoko.kidsbankingandroid.ui.auth.components.DynamicForm
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validateBirthDateRaw
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validateCyrillic
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validateLogin
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validatePassword
import edu.kinoko.kidsbankingandroid.ui.auth.utils.validatePasswordRepeat
import edu.kinoko.kidsbankingandroid.ui.components.Header
import edu.kinoko.kidsbankingandroid.ui.components.Modal
import edu.kinoko.kidsbankingandroid.ui.theme.Secondary

@Composable
fun RegistrationScreen(
    home: () -> Unit,
    auth: () -> Unit,
) {
    val vm: AuthViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = AuthViewModel.factory())
    val uiState by vm.ui.collectAsStateWithLifecycle()

    var step by remember { mutableIntStateOf(1) }
    var formValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var errors by remember { mutableStateOf<Map<String, String?>>(emptyMap()) }
    var touched by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showAll by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) home()
    }

    val authFields = listOf(
        FieldConfig(AuthFieldNames.LOGIN, "Логин"),
        FieldConfig(AuthFieldNames.PASSWORD, "Пароль", isPassword = true),
        FieldConfig(
            AuthFieldNames.REPEAT_PASSWORD,
            "Повторите пароль",
            isPassword = true,
            isChangeble = false
        )
    )
    val userInfoFields = listOf(
        FieldConfig(AuthFieldNames.SURNAME, "Фамилия"),
        FieldConfig(AuthFieldNames.NAME, "Имя"),
        FieldConfig(AuthFieldNames.PATRONYMIC, "Отчество"),
        FieldConfig(AuthFieldNames.BIRTH_DATE, "Дата рождения", isDate = true),
        FieldConfig(AuthFieldNames.CITY, "Город")
    )

    fun validateStep1(vals: Map<String, String>): Map<String, String?> {
        val login = vals[AuthFieldNames.LOGIN].orEmpty()
        val p1 = vals[AuthFieldNames.PASSWORD].orEmpty()
        val p2 = vals[AuthFieldNames.REPEAT_PASSWORD].orEmpty()
        val res = mutableMapOf(
            AuthFieldNames.LOGIN to validateLogin(login),
            AuthFieldNames.PASSWORD to validatePassword(p1),
            AuthFieldNames.REPEAT_PASSWORD to validatePasswordRepeat(p1, p2),
        )
        return res
    }

    fun validateStep2(vals: Map<String, String>): Map<String, String?> {
        val map = mutableMapOf(
            AuthFieldNames.SURNAME to validateCyrillic(vals[AuthFieldNames.SURNAME].orEmpty()),
            AuthFieldNames.NAME to validateCyrillic(vals[AuthFieldNames.NAME].orEmpty()),
            AuthFieldNames.PATRONYMIC to validateCyrillic(vals[AuthFieldNames.PATRONYMIC].orEmpty()),
            AuthFieldNames.CITY to validateCyrillic(vals[AuthFieldNames.CITY].orEmpty()),
        )
        val bdRaw = vals[AuthFieldNames.BIRTH_DATE].orEmpty() // 8 цифр
        map[AuthFieldNames.BIRTH_DATE] = validateBirthDateRaw(bdRaw)
        return map
    }

    fun validateAll(vals: Map<String, String>): Map<String, String?> =
        validateStep1(vals) + validateStep2(vals)

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
                        .height(
                            if (step == 1) {
                                480.dp
                            } else {
                                640.dp
                            }
                        )
                        .background(color = Secondary, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column {
                        Header("Регистрация")
                        Spacer(Modifier.size(12.dp))

                        when (step) {
                            1 -> DynamicForm(
                                fields = authFields,
                                values = formValues,
                                errors = errors,
                                touched = if (showAll) {
                                    authFields.map { it.key }.toSet()
                                } else {
                                    touched
                                },
                                onValueChange = { k, v ->
                                    formValues = formValues.toMutableMap().apply { put(k, v) }
                                    touched = touched + k
                                    errors = validateStep1(formValues)
                                    vm.resetError()
                                },
                                onLastImeAction = {
                                    focusManager.clearFocus(force = true)
                                    keyboard?.hide()
                                }
                            )

                            2 -> DynamicForm(
                                fields = userInfoFields,
                                values = formValues,
                                errors = errors,
                                touched = if (showAll) userInfoFields.map { it.key }
                                    .toSet() else touched,
                                onValueChange = { k, v ->
                                    formValues = formValues.toMutableMap().apply { put(k, v) }
                                    touched = touched + k
                                    errors = validateStep2(formValues)
                                    vm.resetError()
                                },
                                onLastImeAction = {
                                    focusManager.clearFocus(force = true)
                                    keyboard?.hide()
                                }
                            )
                        }
                    }

                    Spacer(Modifier.size(16.dp))

                    if (step == 1) {
                        AuthButtonsBlock(
                            buttonText = "Дальше",
                            buttonAction = {
                                showAll = true
                                errors = validateStep1(formValues)
                                if (!errors.values.any { it != null }) {
                                    step = 2
                                    touched = emptySet()
                                    showAll = false
                                }
                            },
                            textButtonText = "Есть аккаунт? Войти",
                            textButtonAction = auth
                        )
                    } else {
                        val loading = uiState is AuthUiState.Loading
                        AuthButtonsBlock(
                            buttonText = if (loading) {
                                "Регистрируем..."
                            } else {
                                "Зарегистрироваться"
                            },
                            buttonAction = {
                                showAll = true
                                errors = validateAll(formValues)
                                if (!errors.values.any { it != null } && !loading) vm.register(
                                    formValues
                                )
                            },
                            textButtonText = "Назад",
                            textButtonAction = {
                                step = 1
                                touched = emptySet()
                                showAll = false
                            }
                        )
                    }
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
