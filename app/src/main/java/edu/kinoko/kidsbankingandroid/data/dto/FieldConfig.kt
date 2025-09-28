package edu.kinoko.kidsbankingandroid.data.dto

data class FieldConfig(
    val key: String,
    val placeholder: String,
    val isPassword: Boolean = false,
    val isChangeble: Boolean = isPassword,
)