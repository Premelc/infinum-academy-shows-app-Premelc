package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    @SerialName("user") val user: User
)

@Serializable
data class LoginErrorResponse(
    @SerialName("success")val success:Boolean,
    @SerialName("errors")val errors:List<String>
)