package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("user") val user: User
)

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") val image_url: String?
)

@Serializable
data class RegisterErrorResponse(
    @SerialName("email") val email: String,
    @SerialName("errors") val errors: List<String>,
    @SerialName("image_url") val imageUrl: String?
)