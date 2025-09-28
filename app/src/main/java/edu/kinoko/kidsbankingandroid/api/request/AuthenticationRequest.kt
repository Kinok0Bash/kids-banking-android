package edu.kinoko.kidsbankingandroid.api.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest (
    var login: String,
    var password: String
)
