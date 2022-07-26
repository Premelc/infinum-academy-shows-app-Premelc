package com.premelc.shows_dominik_premelc.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.premelc.shows_dominik_premelc.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginModelView>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(sharedPreferencesRememberMe, false)) {
            val user = sharedPreferences.getString(sharedPreferencesEmail, "placeholder").toString().substringBefore('@')
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(user)
            findNavController().navigate(directions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isRememberMeChecked.observe(viewLifecycleOwner) { isRememberMeChecked ->
            binding.rememberMeCheckbox.isChecked = isRememberMeChecked
        }
        initializeUI()
    }

    private fun initializeUI() {
        val loginButton = binding.loginButton
        val emailTextView = binding.emailInput
        val passwordTextView = binding.passwordInput
        viewModel.initRememberMeCheckboxListener(binding.rememberMeCheckbox)
        setupLoginValidation(emailTextView, passwordTextView, loginButton)
        setupLoginButton(loginButton)
    }

    private fun setupLoginButton(loginButton: View) {
        loginButton.setOnClickListener {
            sharedPreferences.edit {
                putBoolean(sharedPreferencesRememberMe, binding.rememberMeCheckbox.isChecked)
                putString(sharedPreferencesEmail, binding.emailInput.text.toString())
            }
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(
                binding.emailInput.text.toString().substringBefore('@')
            )
            findNavController().navigate(directions)
        }
    }

    private fun setupLoginValidation(
        emailTextView: TextView,
        passwordTextView: TextView,
        loginButton: View
    ) {
        emailTextView.doOnTextChanged { text, start, before, count ->
            val error = viewModel.checkEmailValidity(emailTextView.text.toString())
            if (error != null) emailTextView.error = getString(error)
            loginButton.isEnabled = viewModel.validateLoginData(
                emailTextView.text.toString(),
                passwordTextView.text.toString()
            )
        }
        passwordTextView.doOnTextChanged { text, start, before, count ->
            val error = viewModel.checkPasswordValidity(passwordTextView.text.toString())
            if (error != null) passwordTextView.error = getString(error)
            loginButton.isEnabled = viewModel.validateLoginData(
                emailTextView.text.toString(),
                passwordTextView.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}