package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Show(
    @SerialName("id") val id: String,
    @SerialName("average_rating") val average_rating: Int?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val image_url: String,
    @SerialName("no_of_reviews") val no_of_reviews: Int,
    @SerialName("title") val title: String
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int
)

@Serializable
data class ShowsErrorResponse(
    @SerialName("errors") val errors: List<String>
)