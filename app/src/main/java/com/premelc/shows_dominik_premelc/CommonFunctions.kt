package com.premelc.shows_dominik_premelc

import androidx.fragment.app.Fragment
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
import okhttp3.MediaType.Companion.toMediaType

const val SHARED_PREFERENCES_FILE_NAME = "SHOWS"
const val SHARED_PREFERENCES_REMEMBER_ME = "REMEMBER_ME"
const val SHARED_PREFERENCES_EMAIL = "EMAIL"
const val SHARED_PREFERENCES_PFP_URL = "URL"
const val SHARED_PREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN"
const val SHARED_PREFERENCES_CLIENT = "CLIENT"
const val SHARED_PREFERENCES_TOKEN_TYPE = "TOKEN_TYPE"
const val PASSWORD_MIN_LENGTH = 6
const val ANIMATION_DURATION: Long = 500
const val TRIANGLE_ROTATION_DEGREES = 360F
val MEDIA_TYPE_JPG = "image/png".toMediaType()

fun Fragment.getAppDatabase(): ShowsDatabase = (requireActivity().application as ShowApplication).database
