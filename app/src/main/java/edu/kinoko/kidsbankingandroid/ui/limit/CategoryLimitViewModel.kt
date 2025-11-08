package edu.kinoko.kidsbankingandroid.ui.limit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.dto.Limit
import edu.kinoko.kidsbankingandroid.data.service.LimitService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class CategoryLimitViewModel(
    private val limitService: LimitService
): ViewModel() {
    private val _ui = MutableStateFlow<UiState>(UiState.Idle)
    val ui: StateFlow<UiState> = _ui

    private val _limits = MutableStateFlow<MutableList<Limit>>(mutableListOf())
    val limits: StateFlow<MutableList<Limit>> = _limits

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

    fun getLimits() {
        _ui.value = UiState.Loading

        viewModelScope.launch {
            try {
                _limits.value = limitService.getUserLimitList().toMutableList()
                _ui.value = UiState.Success
            } catch (e: Exception) {
                showError(e.humanMessage())
            }
        }
    }

    fun saveLimits() {
        _ui.value = UiState.Loading

        viewModelScope.launch {
            try {
                limitService.setUserLimitList(limits.value)
                _ui.value = UiState.Success
            } catch (e: Exception) {
                showError(e.humanMessage())
            }
        }
    }

    fun changeLimit(id: Int) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            _limits.value[id - 1].isLimit = !_limits.value[id - 1].isLimit
        }
        _ui.value = UiState.Success
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
                return CategoryLimitViewModel(
                    Services.limit,
                ) as T
            }
        }
    }
}