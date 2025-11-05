package edu.kinoko.kidsbankingandroid.ui.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.data.enums.GlobalError
import edu.kinoko.kidsbankingandroid.data.service.AuthService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.store.GlobalErrorStore
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface SessionState {
    data object Checking : SessionState
    data object Authed : SessionState
    data object Unauthorized : SessionState
    data object NetworkError : SessionState
}

class SessionViewModel(
    private val auth: AuthService
) : ViewModel() {

    private val _state = MutableStateFlow<SessionState>(SessionState.Checking)
    val state: StateFlow<SessionState> = _state

    fun bootstrap() {
        GlobalErrorStore.globalError = GlobalError.UNKNOWN_ERROR
        if (_state.value != SessionState.Checking) return
        viewModelScope.launch {
            _state.value = try {
                UserStore.userData = auth.whoAmI()
                SessionState.Authed
            } catch (t: Throwable) {
                when {
                    t.isNetworkProblem() -> {
                        GlobalErrorStore.globalError = GlobalError.NETWORK_ERROR
                        SessionState.NetworkError
                    }

                    t.isServerDown() -> {
                        GlobalErrorStore.globalError = GlobalError.SERVER_ERROR
                        SessionState.NetworkError
                    }

                    t.isAuthProblem() -> SessionState.Unauthorized
                    else -> SessionState.Unauthorized
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching { auth.logout() }
            _state.value = SessionState.Unauthorized
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SessionViewModel(Services.auth) as T
            }
        }

        /** ---- helpers ---- **/
        private fun Throwable.isNetworkProblem(): Boolean =
            this is UnknownHostException ||
                    this is SocketTimeoutException ||
                    (cause is IOException)

        private fun Throwable.isServerDown(): Boolean =
            (this as? HttpException)?.code() in 500..599 || this is ConnectException

        private fun Throwable.isAuthProblem(): Boolean =
            (this as? HttpException)?.code() in setOf(401, 403)
    }
}
