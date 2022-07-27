package com.premelc.shows_dominik_premelc.shows

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Shows>,
    @SerialName("meta") val meta: meta
)

@Serializable
data class Shows(
    @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: Float?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("no_of_reviews") val noOfReviews: Int,
    @SerialName("title") val title: String
)

@Serializable
data class meta(
    @SerialName("pagination") val pagination: pagination
)

@Serializable
data class pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int
)

@Serializable
data class ShowsErrorResponse(
    @SerialName("errors")val errors: List<String>
)