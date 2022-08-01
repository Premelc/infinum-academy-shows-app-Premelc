package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponse(
    @SerialName("reviews")
    val reviews: List<Review>,
    @SerialName("meta")
    val meta: Meta
)

@Serializable
data class Review(
    @SerialName("id")
    val id: String,
    @SerialName("comment")
    val comment: String,
    @SerialName("rating")
    val rating: Int,
    @SerialName("show_id")
    val show_id: Int,
    @SerialName("user")
    val user: User
)

@Serializable
data class ReviewsErrorResponse(
    @SerialName("errors")
    val errors: List<String>
)
