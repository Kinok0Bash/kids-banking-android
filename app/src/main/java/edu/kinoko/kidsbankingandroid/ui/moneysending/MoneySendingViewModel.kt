package edu.kinoko.kidsbankingandroid.ui.moneysending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.request.TransferRequest
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.enums.TransactionStatus
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.service.TransactionService
import edu.kinoko.kidsbankingandroid.data.store.BalanceStore
import edu.kinoko.kidsbankingandroid.ui.util.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class MoneySendingViewModel(
    private val transactionService: TransactionService,
) : ViewModel() {
    private val _ui = MutableStateFlow<UiState>(UiState.Idle)
    val ui: StateFlow<UiState> = _ui

    private val _amount = MutableStateFlow(0)
    val amount: StateFlow<Int> = _amount

    fun inputAppend(d: Int): Boolean {
        val attempted = _amount.value * 10 + d
        val limit = BalanceStore.parentBalance
        val clamped = attempted.coerceAtMost(limit)
        val exceeded = attempted > limit
        _amount.value = clamped
        return exceeded
    }

    fun inputBackspace() {
        _amount.value /= 10
    }

    fun inputClear() {
        _amount.value = 0
    }

    fun bootstrap() {
        inputClear()
    }

    fun sendTransaction() {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = transactionService.transfer(
                    TransferRequest(amount.value)
                )
                require(response.status != TransactionStatus.OK)
                _ui.value = UiState.Success
            } catch (ex: Exception) {
                showError(ex.humanMessage())
            }
        }
    }

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
            is IllegalStateException -> "Ошибка при отправке средств ребенку"
            is IOException -> "Проблема с сетью"
            else -> message ?: "Неизвестная ошибка"
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MoneySendingViewModel(
                    Services.transaction,
                ) as T
            }
        }
    }
}