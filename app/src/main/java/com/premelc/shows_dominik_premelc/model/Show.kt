package com.premelc.shows_dominik_premelc.model

import androidx.annotation.DrawableRes

data class Show (
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int
)