package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetailsResponse(
    @SerialName("show") val show: Show
)

@Serializable
data class ShowDetailsErrorResponse(
    @SerialName("errors") val errors: List<String>
)