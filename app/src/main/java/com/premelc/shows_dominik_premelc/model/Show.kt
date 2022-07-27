package com.premelc.shows_dominik_premelc.model

import androidx.annotation.DrawableRes
import com.premelc.shows_dominik_premelc.R

data class Show(
    val id: String,
    val name: String,
    val description: String?,
    var reviews: List<Review>,
    val imageUrl: String?
    // @DrawableRes val imageResourceId: Int = R.mipmap.pfp
)