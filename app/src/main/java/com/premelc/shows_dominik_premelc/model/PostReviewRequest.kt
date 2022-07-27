package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewRequest(
    @SerialName("rating")val rating: Int,
    @SerialName("comment")val comment:String,
    @SerialName("show_id")val show_id:Int
)

