package com.premelc.shows_dominik_premelc.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
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
        sharedPreferences = requireContext().getSharedPreferences("SHOWS", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("REMEMBER_ME", false)) {
            val user = sharedPreferences.getString("EMAIL", "placeholder")?.substringBefore('@')
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(user!!)
            findNavController().navigate(directions)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        viewModel.setupLoginValidation(emailTextView, passwordTextView, loginButton)
        setupLoginButton(loginButton)
    }

    private fun setupLoginButton(loginButton: View) {
        loginButton.setOnClickListener {
            sharedPreferences.edit {
                putBoolean("REMEMBER_ME", binding.rememberMeCheckbox.isChecked)
                putString("EMAIL", binding.emailInput.text.toString())
            }
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(binding.emailInput.text.toString().substringBefore('@'))
            findNavController().navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}