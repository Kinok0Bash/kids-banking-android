package edu.kinoko.kidsbankingandroid.api.response

import edu.kinoko.kidsbankingandroid.data.dto.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse (
    var token: String,
    var user: User
)
