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

const val SHARED_PREFERENCES_FILE_NAME = "SHOWS"
const val SHARED_PREFERENCES_REMEMBER_ME = "REMEMBER_ME"
const val SHARED_PREFERENCES_EMAIL = "EMAIL"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(SHARED_PREFERENCES_REMEMBER_ME, false)) {
            val user = sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, "placeholder").toString().substringBefore('@')
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
        viewModel.emailValidityStringCode.observe(viewLifecycleOwner) { emailValidityStringCode ->
            if (emailValidityStringCode != null) binding.emailInput.error = getString(emailValidityStringCode)
        }
        viewModel.passwordValidityStringCode.observe(viewLifecycleOwner) { passwordValidityStringCode ->
            if (passwordValidityStringCode != null) binding.passwordInput.error = getString(passwordValidityStringCode)
        }
        viewModel.loginButtonIsEnabled.observe(viewLifecycleOwner) { loginButtonIsEnabled ->
            binding.loginButton.isEnabled = loginButtonIsEnabled
        }
        initializeUI()
    }

    private fun initializeUI() {
        viewModel.initRememberMeCheckboxListener(binding.rememberMeCheckbox)
        setupLoginValidation()
        setupLoginButton()
        setupRegisterButton()
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            sharedPreferences.edit {
                putBoolean(SHARED_PREFERENCES_REMEMBER_ME, binding.rememberMeCheckbox.isChecked)
                putString(SHARED_PREFERENCES_EMAIL, binding.emailInput.text.toString())
            }
            val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(
                binding.emailInput.text.toString().substringBefore('@')
            )
            findNavController().navigate(directions)
        }
    }

    private fun setupRegisterButton() {
        binding.registerButton.setOnClickListener {
            val directions = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(directions)
        }
    }

    private fun setupLoginValidation() {
        viewModel.initLoginTextInputListeners(binding.emailInput, binding.passwordInput)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}