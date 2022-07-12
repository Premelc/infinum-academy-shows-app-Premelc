package loginFunctions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

fun setupLoginValidation(emailTextView: TextView, passwordTextView: TextView, loginButton: View){
        emailTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(!validateEmail(s.toString())){
                   emailTextView.error = "Invalid email address"
                }
                loginButton.isEnabled = validateLoginData(emailTextView.text.toString() , passwordTextView.text.toString())
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }})
        passwordTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(!validatePassword(s.toString())){
                    passwordTextView.error = "Invalid password"
                }
                loginButton.isEnabled = validateLoginData(emailTextView.text.toString() , passwordTextView.text.toString())
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
    }

fun setupLoginButton(context:Context ,loginButton: View , emailTextView: TextView) {
    loginButton.setOnClickListener {
        //EXPLICIT INTENT
        /*
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("username" , email.substringBefore('@'))
        startActivity(intent)
        */
        //IMPLICIT INTENT
        val intent = Intent(ACTION_SEND).apply {
            putExtra("username", emailTextView.text.toString().substringBefore('@'))
            type = "text/plain"
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //DEFAULT APP
        }
    }
}
