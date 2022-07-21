package com.premelc.shows_dominik_premelc.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.premelc.shows_dominik_premelc.databinding.FragmentLoginBinding
import com.premelc.shows_dominik_premelc.login.loginFunctions.validateEmail
import com.premelc.shows_dominik_premelc.login.loginFunctions.validateLoginData
import com.premelc.shows_dominik_premelc.login.loginFunctions.validatePassword

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        val loginButton = binding.loginButton
        val emailTextView = binding.emailInput
        val passwordTextView = binding.passwordInput
        setupLoginValidation(emailTextView, passwordTextView, loginButton)
        setupLoginButton(loginButton)
    }

    private fun setupLoginButton(loginButton: View) {
        loginButton.setOnClickListener {
            val user = binding.emailInput.text.toString().substringBefore('@')
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(user)
            findNavController().navigate(directions)
        }
    }

    private fun checkEmailRegex(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validateEmail(emailTextView.text.toString())) {
            emailTextView.error = "Invalid email address"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    private fun checkPassword(emailTextView: TextView, passwordTextView: TextView, loginButton: View) {
        if (!validatePassword(passwordTextView.text.toString())) {
            passwordTextView.error = "Invalid password"
        }
        loginButton.isEnabled = validateLoginData(
            emailTextView.text.toString(),
            passwordTextView.text.toString()
        )
    }

    private fun setupLoginValidation(
        emailTextView: TextView,
        passwordTextView: TextView,
        loginButton: View
    ) {
        emailTextView.doOnTextChanged { text, start, before, count -> checkEmailRegex(emailTextView, passwordTextView, loginButton) }
        passwordTextView.doOnTextChanged { text, start, before, count -> checkPassword(emailTextView, passwordTextView, loginButton) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}