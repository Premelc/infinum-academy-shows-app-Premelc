package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetailsRequest(
    @SerialName("id") val id: String
)