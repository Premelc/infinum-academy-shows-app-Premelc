package com.premelc.shows_dominik_premelc.login.loginFunctions

private const val PASSWORD_MIN_LENGTH = 6

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): Boolean {
    return password.length >= PASSWORD_MIN_LENGTH
}

fun validateLoginData(email: String, password: String): Boolean {
    return validateEmail(email) && validatePassword(password)
}