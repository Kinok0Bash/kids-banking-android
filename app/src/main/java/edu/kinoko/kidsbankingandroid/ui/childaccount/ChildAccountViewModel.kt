package edu.kinoko.kidsbankingandroid.ui.childaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.service.BalanceService
import edu.kinoko.kidsbankingandroid.data.service.Services
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed interface ChildAccountUiState {
    data object Idle : ChildAccountUiState
    data object Loading : ChildAccountUiState
    data class Error(val message: String) : ChildAccountUiState
    data object Success : ChildAccountUiState
}

class ChildAccountViewModel(
    private val balanceService: BalanceService,
) : ViewModel() {

    private val _ui = MutableStateFlow<ChildAccountUiState>(ChildAccountUiState.Idle)
    val ui: StateFlow<ChildAccountUiState> = _ui

    private var errorJob: Job? = null

    private fun showError(msg: String) {
        _ui.value = ChildAccountUiState.Error(msg)
        errorJob?.cancel()
        errorJob = viewModelScope.launch {
            delay(3000)
            // чтобы не затереть новую ошибку, проверим что всё ещё та же
            if (_ui.value is ChildAccountUiState.Error &&
                (_ui.value as ChildAccountUiState.Error).message == msg
            ) {
                _ui.value = ChildAccountUiState.Idle
            }
        }
    }

    fun bootstrap() {
        _ui.value = ChildAccountUiState.Loading
        viewModelScope.launch {
            try {
                balanceService.getParentBalance()
                _ui.value = ChildAccountUiState.Success
            } catch (ex: Exception) {
                showError(ex.humanMessage())
            }
        }
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
                return ChildAccountViewModel(
                    Services.balance,
                ) as T
            }
        }
    }
}