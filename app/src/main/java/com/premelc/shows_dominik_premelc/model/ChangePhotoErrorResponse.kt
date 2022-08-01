package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePhotoErrorResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("errors") val errors: List<String>,
    @SerialName("status") val status: String
)
