package com.premelc.shows_dominik_premelc.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.ANIMATION_DURATION
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_ACCESS_TOKEN
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_CLIENT
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_EMAIL
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_FILE_NAME
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_PFP_URL
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_REMEMBER_ME
import com.premelc.shows_dominik_premelc.SHARED_PREFERENCES_TOKEN_TYPE
import com.premelc.shows_dominik_premelc.TRIANGLE_ROTATION_DEGREES
import com.premelc.shows_dominik_premelc.databinding.FragmentLoginBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RequestResponseBottomSheetBinding
import com.premelc.shows_dominik_premelc.login.viewModel.LoginViewModel
import com.premelc.shows_dominik_premelc.networking.ApiModule

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<LoginFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiModule.initRetrofit(requireContext(), emptyMap())
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
            binding.emailLayout.error = if (emailValidityStringCode != null) getString(emailValidityStringCode)
            else null
        }
        viewModel.passwordValidityStringCode.observe(viewLifecycleOwner) { passwordValidityStringCode ->
            binding.passwordLayout.error =  if (passwordValidityStringCode != null) getString(passwordValidityStringCode)
            else null
        }
        viewModel.loginButtonIsEnabled.observe(viewLifecycleOwner) { loginButtonIsEnabled ->
            binding.loginButton.isEnabled = loginButtonIsEnabled
        }
        viewModel.loginResponse.observe(viewLifecycleOwner) { loginResponse ->
            if (loginResponse) {
                dialog.dismiss()
                val directions = LoginFragmentDirections.actionLoginFragmentToShowsFragment(
                    binding.emailInput.text.toString()
                )
                findNavController().navigate(directions)
            } else {
                triggerNotificationBottomSheet(R.drawable.fail, getString(R.string.login_failed), getString(R.string.connection_error))
            }
        }
        viewModel.loginErrorMessage.observe(viewLifecycleOwner) { loginErrorMessage ->
            triggerNotificationBottomSheet(R.drawable.fail, getString(R.string.login_failed), loginErrorMessage)
        }
        viewModel.headerValues.observe(viewLifecycleOwner) { headerValues ->
            sharedPreferences.edit(commit = true) {
                putString(SHARED_PREFERENCES_TOKEN_TYPE, headerValues[SHARED_PREFERENCES_TOKEN_TYPE])
                putString(SHARED_PREFERENCES_ACCESS_TOKEN, headerValues[SHARED_PREFERENCES_ACCESS_TOKEN])
                putString(SHARED_PREFERENCES_CLIENT, headerValues[SHARED_PREFERENCES_CLIENT])
                putString(SHARED_PREFERENCES_PFP_URL, headerValues[SHARED_PREFERENCES_PFP_URL])
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
        animateLogo()
        binding.rememberMeCheckbox.setOnCheckedChangeListener{ _ , isChecked ->
            viewModel.initRememberMeCheckboxListener(isChecked)
        }
        setupLoginValidation()
        setupLoginButton()
        setupRegisterButton()
    }

    private fun animateLogo() {
        binding.triangleImg.animate()
            .translationY(0F)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(BounceInterpolator())
            .start()

        binding.titleText.animate()
            .scaleXBy(1F)
            .scaleYBy(1F)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(LinearInterpolator())
            .setStartDelay(ANIMATION_DURATION)
            .start()
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
            binding.triangleImg.animate().rotation(TRIANGLE_ROTATION_DEGREES).setDuration(ANIMATION_DURATION)
                .setInterpolator(OvershootInterpolator()).start()
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
        with(binding) {
            emailInput.doOnTextChanged { text, start, before, count ->
                viewModel.checkEmailValidity(emailInput.text.toString())
                viewModel.validateLoginData(emailInput.text.toString(), passwordInput.text.toString())
            }
            passwordInput.doOnTextChanged { text, start, before, count ->
                viewModel.checkPasswordValidity(passwordInput.text.toString())
                viewModel.validateLoginData(emailInput.text.toString(), passwordInput.text.toString())
            }
        }
    }

    private fun triggerNotificationBottomSheet(icon: Int, title: String, subtitle: String) {
        dialog.dismiss()
        val bottomSheetBinding: RequestResponseBottomSheetBinding = RequestResponseBottomSheetBinding.inflate(layoutInflater)
        with(bottomSheetBinding) {
            callbackIcon.setImageResource(icon)
            callbackText.text = title
            callbackDescription.text = subtitle
        }
        dialog.setContentView(bottomSheetBinding.root)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}