package com.premelc.shows_dominik_premelc.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class Review(
    val id: String,
    val username: String,
    val grade: Int,
    val text: String,
    @DrawableRes val profilePic: Int
):Serializable