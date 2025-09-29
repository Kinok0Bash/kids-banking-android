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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

sealed interface HomeScreenUiState {
    data object Idle : HomeScreenUiState
    data object Loading : HomeScreenUiState
    data class Error(val message: String) : HomeScreenUiState
    data object Success : HomeScreenUiState
}

class HomeScreenViewModel(
    private val balanceService: BalanceService,
    private val transactionService: TransactionService,
    private val parentService: ParentService,
) : ViewModel() {

    private val _ui = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Idle)
    val ui: StateFlow<HomeScreenUiState> = _ui

    fun bootstrap() {
        _ui.value = HomeScreenUiState.Loading
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
                if (UserStore.userData.isGetKid) {
                    transactionService.getLastTransactions()
                }
                _ui.value = HomeScreenUiState.Success
            } catch (ex: Exception) {
                _ui.value = HomeScreenUiState.Error(ex.humanMessage())
            }
        }
    }

    fun getSalary() {
        _ui.value = HomeScreenUiState.Loading
        viewModelScope.launch {
            try {
                parentService.getSalary()
                balanceService.getParentBalance()
                _ui.value = HomeScreenUiState.Success
            } catch (ex: Exception) {
                _ui.value = HomeScreenUiState.Error(ex.humanMessage())
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