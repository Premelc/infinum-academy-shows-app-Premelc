package com.premelc.shows_dominik_premelc.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Review(
    val id: String = "placeholder",
    val username: String = "placeholder",
    val grade: Float,
    val text: String,
    @DrawableRes val profilePic: Int
) : Serializable