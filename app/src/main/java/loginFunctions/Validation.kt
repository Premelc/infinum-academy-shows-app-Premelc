package loginFunctions

    fun validateEmail(email: String) : Boolean {

        if (email.isEmpty()) return false
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    fun validatePassword(password: String) : Boolean{
        if (password.length < 6)return false;
        return true;
    }