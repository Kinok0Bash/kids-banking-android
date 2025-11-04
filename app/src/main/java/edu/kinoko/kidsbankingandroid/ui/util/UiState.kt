package edu.kinoko.kidsbankingandroid.ui.util

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Error(val message: String) : UiState
    data object Success : UiState
}