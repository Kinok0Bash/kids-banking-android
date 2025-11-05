package edu.kinoko.kidsbankingandroid.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.dto.Transaction
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.service.TransactionService
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class HistoryScreenViewModel(
    private val transactionService: TransactionService
) : ViewModel() {
    private val _ui = MutableStateFlow<UiState>(UiState.Idle)
    val ui: StateFlow<UiState> = _ui

    private val _transactionGroups =
        MutableStateFlow<LinkedHashMap<LocalDate, List<Transaction>>>(linkedMapOf())
    val transactionGroups: StateFlow<LinkedHashMap<LocalDate, List<Transaction>>> = _transactionGroups

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

    fun bootstrap() {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                val resp = transactionService.getAllTransactions()
                _transactionGroups.value = resp
                    .groupBy { it.date.toJavaLocalDateTime().toLocalDate().toKotlinLocalDate() }
                    .toList()
                    .sortedByDescending { (date, _) -> date }
                    .toMap(LinkedHashMap())
                _ui.value = UiState.Success
            } catch (ex: Exception) {
                showError(ex.humanMessage())
            }
        }
    }

    private fun Exception.humanMessage(): String {
        Log.e("HystoryScreen", this.stackTraceToString())
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
                return HistoryScreenViewModel(Services.transaction) as T
            }
        }
    }
}