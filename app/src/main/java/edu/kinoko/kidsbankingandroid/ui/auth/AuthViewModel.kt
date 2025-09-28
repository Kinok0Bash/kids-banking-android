package edu.kinoko.kidsbankingandroid.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.request.AuthenticationRequest
import edu.kinoko.kidsbankingandroid.api.request.RegistrationRequest
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.constants.AuthFieldNames
import edu.kinoko.kidsbankingandroid.data.service.AuthService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.ui.auth.utils.parseRawDdMmYyyy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
    data object Success : AuthUiState
}

class AuthViewModel(
    private val svc: AuthService
) : ViewModel() {

    private val _ui = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val ui: StateFlow<AuthUiState> = _ui

    fun login(values: Map<String, String>) {
        val login = values[AuthFieldNames.LOGIN].orEmpty()
        val pass = values[AuthFieldNames.PASSWORD].orEmpty()
        if (login.isBlank() || pass.isBlank()) {
            _ui.value = AuthUiState.Error("Заполни логин и пароль")
            return
        }
        _ui.value = AuthUiState.Loading
        viewModelScope.launch {
            try {
                svc.login(AuthenticationRequest(login = login, password = pass))
                _ui.value = AuthUiState.Success
            } catch (e: Exception) {
                _ui.value = AuthUiState.Error(e.humanMessage())
            }
        }
    }

    fun register(values: Map<String, String>) {
        val login = values[AuthFieldNames.LOGIN].orEmpty()
        val pass = values[AuthFieldNames.PASSWORD].orEmpty()
        val pass2 = values[AuthFieldNames.REPEAT_PASSWORD].orEmpty()
        if (login.isBlank() || pass.isBlank()) {
            _ui.value = AuthUiState.Error("Заполни логин и пароль")
            return
        }
        if (pass != pass2) {
            _ui.value = AuthUiState.Error("Пароли не совпадают")
            return
        }

        val birthDate = parseRawDdMmYyyy(values[AuthFieldNames.BIRTH_DATE].orEmpty())
        if (birthDate == null) {
            _ui.value = AuthUiState.Error("Дата введена не в верном формате (dd.MM.yyyy)")
            return
        }

        val req = RegistrationRequest(
            username = login,
            password = pass,
            lastname = values[AuthFieldNames.SURNAME].orEmpty(),
            name = values[AuthFieldNames.NAME].orEmpty(),
            fatherName = values[AuthFieldNames.PATRONYMIC].orEmpty(),
            birthDate = birthDate,
            city = values[AuthFieldNames.CITY].orEmpty()
        )

        _ui.value = AuthUiState.Loading
        viewModelScope.launch {
            try {
                svc.register(req)
                _ui.value = AuthUiState.Success
            } catch (e: Exception) {
                _ui.value = AuthUiState.Error(e.humanMessage())
            }
        }
    }

    fun resetError() {
        if (_ui.value is AuthUiState.Error) _ui.value = AuthUiState.Idle
    }

    private fun Exception.humanMessage(): String {
        Log.e("AuthViewModel", this.stackTraceToString())
        return when (this) {
            is HttpException -> {
                Json.decodeFromString<ErrorResponse>(
                    response()
                        ?.errorBody()
                        ?.string()
                        ?: "{\"error\":\"Неизвестная ошибка\"}"
                ).error
            }

            is IOException -> "Проблема с сетью"
            else -> message ?: "Неизвестная ошибка"
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(Services.auth) as T
            }
        }
    }
}
