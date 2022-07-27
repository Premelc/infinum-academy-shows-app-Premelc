package com.premelc.shows_dominik_premelc.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentLoginBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RequestResponseBottomSheetBinding
import com.premelc.shows_dominik_premelc.networking.ApiModule

const val SHARED_PREFERENCES_FILE_NAME = "SHOWS"
const val SHARED_PREFERENCES_REMEMBER_ME = "REMEMBER_ME"
const val SHARED_PREFERENCES_EMAIL = "EMAIL"
const val SHARED_PREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN"
const val SHARED_PREFERENCES_CLIENT = "CLIENT"
const val SHARED_PREFERENCES_TOKEN_TYPE = "TOKEN_TYPE"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<LoginFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiModule.initRetrofit(requireContext(), emptyList())
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(SHARED_PREFERENCES_REMEMBER_ME, false)) {
            val user = sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, "placeholder").toString()
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
        dialog = BottomSheetDialog(requireContext())
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
        viewModel.loginResponse.observe(viewLifecycleOwner) { loginResponse ->
            if (viewModel.validateEmail(loginResponse)) {
                val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(
                    binding.emailInput.text.toString()
                )
                dialog.dismiss()
                findNavController().navigate(directions)
            } else {
                dialog.dismiss()
                val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding) {
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.login_failed)
                    callbackDescription.text = loginResponse
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }
        }
        viewModel.headerValues.observe(viewLifecycleOwner) { headerValues ->
            with(sharedPreferences.edit()) {
                putString(SHARED_PREFERENCES_TOKEN_TYPE, headerValues[0])
                putString(SHARED_PREFERENCES_ACCESS_TOKEN, headerValues[1])
                putString(SHARED_PREFERENCES_CLIENT, headerValues[2]).commit()
            }
        }
        handleRegisterSuccessful()
        initializeUI()
    }

    private fun handleRegisterSuccessful() {
        if (args.fromRegisterSuccessful) {
            binding.LoginText.text = getString(R.string.registerSuccessTitle)
            binding.registerButton.isVisible = false
            val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
            bottomSheetBinding.callbackText.text = getString(R.string.registerSuccessTitle)
            bottomSheetBinding.callbackIcon.setImageResource(R.drawable.success)
            dialog.setContentView(bottomSheetBinding.root)
            dialog.show()
        }
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
            viewModel.onLoginButtonClicked(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )

            val loadingBottomSheetBinding: LoadingBottomSheetBinding = LoadingBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(loadingBottomSheetBinding.root)
            dialog.show()
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