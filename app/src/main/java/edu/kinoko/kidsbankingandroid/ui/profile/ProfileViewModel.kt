package edu.kinoko.kidsbankingandroid.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.service.AuthService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(
    private val authService: AuthService
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
    
    fun getProfileInfo() {
        _ui.value = UiState.Loading

        viewModelScope.launch {
            try {
                UserStore.userData = authService.whoAmI()
                _ui.value = UiState.Success
            } catch (e: Exception) {
                showError(e.humanMessage())
            }
        }
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
                return ProfileViewModel(
                    Services.auth,
                ) as T
            }
        }
    }
}