package edu.kinoko.kidsbankingandroid.data.dto

import edu.kinoko.kidsbankingandroid.data.enums.Role
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Contextual
    val id: UUID,
    val fullName: String,
    val lastname: String,
    val name: String,
    val fatherName: String,
    val username: String,
    val birthDate: LocalDate,
    val city: String,
    val role: Role,
    val isGetKid: Boolean,
)
