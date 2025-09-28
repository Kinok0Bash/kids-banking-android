package edu.kinoko.kidsbankingandroid.api.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val lastname: String,
    val name: String,
    val fatherName: String,
    val username: String,
    val password: String,
    val birthDate: LocalDate,
    val city: String
)
