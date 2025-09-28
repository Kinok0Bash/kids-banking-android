package edu.kinoko.kidsbankingandroid.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.kinoko.kidsbankingandroid.data.service.AuthService
import edu.kinoko.kidsbankingandroid.data.service.Services
import edu.kinoko.kidsbankingandroid.data.store.UserStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface SessionState {
    data object Checking : SessionState
    data object Authed : SessionState
    data object Guest : SessionState
}

class SessionViewModel(
    private val auth: AuthService
) : ViewModel() {

    private val _state = MutableStateFlow<SessionState>(SessionState.Checking)
    val state: StateFlow<SessionState> = _state

    fun bootstrap() {
        if (_state.value != SessionState.Checking) return
        viewModelScope.launch {
            _state.value = try {
                UserStore.userData = auth.whoAmI()
                SessionState.Authed
            } catch (_: Exception) {
                SessionState.Guest
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching { auth.logout() }
            _state.value = SessionState.Guest
        }
    }

    companion object {
        fun factory() = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SessionViewModel(Services.auth) as T
            }
        }
    }
}
