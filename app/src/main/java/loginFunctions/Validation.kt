package loginFunctions

    fun validateEmail(email: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String) : Boolean{
        return password.length >= 6
    }

fun validateLoginData(email:String,password:String): Boolean{
    return validateEmail(email) && validatePassword(password)
}