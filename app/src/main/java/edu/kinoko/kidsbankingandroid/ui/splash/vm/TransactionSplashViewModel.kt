package edu.kinoko.kidsbankingandroid.ui.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.kinoko.kidsbankingandroid.api.request.PayRequest
import edu.kinoko.kidsbankingandroid.api.request.TransferRequest
import edu.kinoko.kidsbankingandroid.data.constants.AppRoutes
import edu.kinoko.kidsbankingandroid.data.enums.TransactionStatus
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.service.TransactionService

class TransactionSplashViewModel(
    private val transactionService: TransactionService
) : ViewModel() {

    suspend fun transfer(amount: Int): String {
        return try {
            val resp = transactionService.transfer(TransferRequest(amount))
            "${AppRoutes.TRANSACTION_STATUS}?status=${resp.status.name}&sum=${resp.sum}"
        } catch (_: Exception) {
            "${AppRoutes.TRANSACTION_STATUS}?status=${TransactionStatus.FAIL.name}&sum=-1"
        }
    }

    suspend fun pay(request: PayRequest): String {
        return try {
            val resp = transactionService.pay(request)
            "${AppRoutes.TRANSACTION_STATUS}?status=${resp.status.name}&sum=${resp.sum}"
        } catch (_: Exception) {
            "${AppRoutes.TRANSACTION_STATUS}?status=${TransactionStatus.FAIL.name}&sum=-1"
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionSplashViewModel(Services.transaction) as T
            }
        }
    }
}