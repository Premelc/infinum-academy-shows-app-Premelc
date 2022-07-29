package com.premelc.shows_dominik_premelc

import com.premelc.shows_dominik_premelc.login.PASSWORD_MIN_LENGTH

object CommonFunctions {

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

}