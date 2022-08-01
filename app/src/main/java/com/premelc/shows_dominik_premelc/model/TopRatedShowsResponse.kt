package com.premelc.shows_dominik_premelc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedShowsResponse(
    @SerialName("shows")val shows:List<Show>
)