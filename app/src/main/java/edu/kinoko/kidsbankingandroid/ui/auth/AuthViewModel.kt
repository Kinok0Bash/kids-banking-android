package edu.kinoko.kidsbankingandroid.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.request.AuthenticationRequest
import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.service.AuthService
import edu.kinoko.kidsbankingandroid.data.service.ParentService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.auth.utils.parseRawDdMmYyyy
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(
    private val authService: AuthService,
    private val parentService: ParentService,
) : ViewModel() {

    private val _ui = MutableStateFlow<UiState>(UiState.Idle)
    val ui: StateFlow<UiState> = _ui

    private var errorJob: Job? = null

    private fun showError(msg: String) {
        _ui.value = UiState.Error(msg)
        errorJob?.cancel()
        errorJob = viewModelScope.launch {
            delay(3000)
            // чтобы не затереть новую ошибку, проверим что всё ещё та же
            if (_ui.value is UiState.Error &&
                (_ui.value as UiState.Error).message == msg
            ) {
                _ui.value = UiState.Idle
            }
        }
    }

    fun login(values: Map<String, String>) {
        val login = values[AuthFieldNames.LOGIN].orEmpty()
        val pass = values[AuthFieldNames.PASSWORD].orEmpty()
        if (login.isBlank() || pass.isBlank()) {
            showError("Заполни логин и пароль")
            return
        }
        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = authService.login(
                    AuthenticationRequest(
                        login = login,
                        password = pass
                    )
                )
                UserStore.userData = response.user
                _ui.value = UiState.Success
            } catch (e: Exception) {
                showError(e.humanMessage())
            }
        }
    }

    fun register(values: Map<String, String>, regType: Role) {
        val login = values[AuthFieldNames.LOGIN].orEmpty()
        val password = values[AuthFieldNames.PASSWORD].orEmpty()
        val password2 = values[AuthFieldNames.REPEAT_PASSWORD].orEmpty()
        if (login.isBlank() || password.isBlank()) {
            showError("Заполни логин и пароль")
            return
        }
        if (password != password2) {
            showError("Пароли не совпадают")
            return
        }

        val birthDate = parseRawDdMmYyyy(values[AuthFieldNames.BIRTH_DATE].orEmpty())
        if (birthDate == null) {
            showError("Дата введена не в верном формате (dd.MM.yyyy)")
            return
        }

        val request = RegistrationRequest(
            username = login,
            password = password,
            lastname = values[AuthFieldNames.SURNAME].orEmpty(),
            name = values[AuthFieldNames.NAME].orEmpty(),
            fatherName = values[AuthFieldNames.PATRONYMIC].orEmpty(),
            birthDate = birthDate,
            city = values[AuthFieldNames.CITY].orEmpty()
        )

        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                when (regType) {
                    Role.PARENT -> {
                        val response = authService.register(request)
                        UserStore.userData = response.user
                    }
                    Role.CHILD -> {
                        parentService.createChildAccount(request)
                    }
                }
                _ui.value = UiState.Success
            } catch (e: Exception) {
                _ui.value = UiState.Error(e.humanMessage())
            }
        }
    }

    fun resetError() {
        if (_ui.value is UiState.Error) _ui.value = UiState.Idle
    }

    private fun Exception.humanMessage(): String {
        Log.e("AuthViewModel", this.stackTraceToString())
        return when (this) {
            is HttpException -> {
                try {
                    Json.decodeFromString<ErrorResponse>(
                        response()
                            ?.errorBody()
                            ?.string()
                            ?: "{\"error\":\"Неизвестная ошибка\"}"
                    ).error
                } catch (_: Exception) {
                    "Сервер недоступен. Попробуйте еще раз позже"
                }
            }

            is IOException -> "Проблема с сетью"
            else -> message ?: "Неизвестная ошибка"
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(
                    authService = Services.auth,
                    parentService = Services.parent,
                ) as T
            }
        }
    }
}
