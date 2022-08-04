package com.premelc.shows_dominik_premelc.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.ANIMATION_DURATION
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.TRIANGLE_ROTATION_DEGREES
import com.premelc.shows_dominik_premelc.databinding.FragmentRegisterBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RequestResponseBottomSheetBinding
import com.premelc.shows_dominik_premelc.networking.ApiModule
import com.premelc.shows_dominik_premelc.register.viewModel.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        ApiModule.initRetrofit(requireContext(), emptyMap())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)
        dialog = BottomSheetDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.emailValidityStringCode.observe(viewLifecycleOwner) { emailValidityStringCode ->
            binding.emailLayout.error = if (emailValidityStringCode != null) getString(emailValidityStringCode)
            else null
        }
        viewModel.passwordValidityStringCode.observe(viewLifecycleOwner) { passwordValidityStringCode ->
            binding.passwordLayout.error = if (passwordValidityStringCode != null) getString(passwordValidityStringCode)
            else null
        }
        viewModel.repeatPasswordValidityStringCode.observe(viewLifecycleOwner) { repeatPasswordValidityStringCode ->
            binding.repeatPasswordLayout.error = if (repeatPasswordValidityStringCode != null) getString(repeatPasswordValidityStringCode)
            else null
        }
        viewModel.passwordsMatchStringCode.observe(viewLifecycleOwner) { passwordsMatch ->
            binding.repeatPasswordLayout.error = if (passwordsMatch != null) getString(passwordsMatch)
            else null
        }
        viewModel.registerButtonIsEnabled.observe(viewLifecycleOwner) { registerButtonIsEnabled ->
            binding.registerButton.isEnabled = registerButtonIsEnabled
        }
        viewModel.registerResponse.observe(viewLifecycleOwner) { registerResponse ->
            if (registerResponse) {
                dialog.dismiss()
                val directions = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(true)
                findNavController().navigate(directions)
            } else {
                triggerNotificationBottomSheet(
                    R.drawable.fail,
                    getString(R.string.registration_failed),
                    getString(R.string.connection_error)
                )
            }
        }
        viewModel.registerErrorMessage.observe(viewLifecycleOwner) { registerErrorMessage ->
            triggerNotificationBottomSheet(R.drawable.fail, getString(R.string.registration_failed), registerErrorMessage)
        }
        initializeUI()
    }

    private fun initializeUI() {
        animateLogo()
        setUpEmailAndPasswordValidation()
        setUpRegisterButton()
    }

    private fun setUpEmailAndPasswordValidation() {
        with(binding) {
            emailInput.doOnTextChanged { text, start, before, count ->
                viewModel.checkEmailValidity(emailInput.text.toString())
                viewModel.validateRegisterData(
                    emailInput.text.toString(),
                    passwordInput.text.toString(),
                    repeatPasswordInput.text.toString()
                )
            }
            passwordInput.doOnTextChanged { text, start, before, count ->
                viewModel.checkPasswordValidity(passwordInput.text.toString())
                viewModel.validateRegisterData(
                    emailInput.text.toString(),
                    passwordInput.text.toString(),
                    repeatPasswordInput.text.toString()
                )
            }
            repeatPasswordInput.doOnTextChanged { text, start, before, count ->
                viewModel.checkRepeatPasswordValidity(repeatPasswordInput.text.toString())
                viewModel.checkIfPasswordsMatch(repeatPasswordInput.text.toString(), passwordInput.text.toString())
                viewModel.validateRegisterData(
                    emailInput.text.toString(),
                    passwordInput.text.toString(),
                    repeatPasswordInput.text.toString()
                )
            }
        }
    }

    private fun setUpRegisterButton() {
        binding.registerButton.setOnClickListener {
            viewModel.onRegisterButtonClicked(
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