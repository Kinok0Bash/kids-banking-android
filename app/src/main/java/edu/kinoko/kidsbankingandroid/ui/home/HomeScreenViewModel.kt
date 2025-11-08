package edu.kinoko.kidsbankingandroid.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.api.response.ErrorResponse
import edu.kinoko.kidsbankingandroid.data.enums.Role
import edu.kinoko.kidsbankingandroid.data.service.BalanceService
import edu.kinoko.kidsbankingandroid.data.service.ParentService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.service.TransactionService
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

class HomeScreenViewModel(
    private val balanceService: BalanceService,
    private val transactionService: TransactionService,
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

    fun bootstrap() {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                when (UserStore.userData.role) {
                    Role.PARENT -> {
                        balanceService.getParentBalance()
                    }

                    Role.CHILD -> {
                        balanceService.getChildBalance()
                    }
                }
                if (
                    (UserStore.userData.role == Role.PARENT && UserStore.userData.isGetKid)
                    || UserStore.userData.role == Role.CHILD
                ) {
                    transactionService.getLastTransactions()
                }
                _ui.value = UiState.Success
            } catch (ex: Exception) {
                showError(ex.humanMessage())
            }
        }
    }

    fun getSalary() {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            try {
                parentService.getSalary()
                balanceService.getParentBalance()
                _ui.value = UiState.Success
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
                return HomeScreenViewModel(
                    Services.balance,
                    Services.transaction,
                    Services.parent,
                ) as T
            }
        }
    }
}