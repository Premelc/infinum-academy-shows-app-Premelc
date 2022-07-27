package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewResponse(
    @SerialName("review") val review: Review
)

@Serializable
data class PostReviewErrorResponse(
    @SerialName("errors") val errors: List<String>
)
