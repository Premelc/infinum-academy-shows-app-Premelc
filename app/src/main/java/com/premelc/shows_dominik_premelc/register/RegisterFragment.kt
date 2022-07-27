package com.premelc.shows_dominik_premelc.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.premelc.shows_dominik_premelc.R
import com.premelc.shows_dominik_premelc.databinding.FragmentRegisterBinding
import com.premelc.shows_dominik_premelc.databinding.LoadingBottomSheetBinding
import com.premelc.shows_dominik_premelc.databinding.RegisterLoginResultBottomSheetBinding
import com.premelc.shows_dominik_premelc.networking.ApiModule

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        ApiModule.initRetrofit(requireContext())
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
            if (emailValidityStringCode != null) binding.emailInput.error = getString(emailValidityStringCode)
        }
        viewModel.passwordValidityStringCode.observe(viewLifecycleOwner) { passwordValidityStringCode ->
            if (passwordValidityStringCode != null) binding.passwordInput.error = getString(passwordValidityStringCode)
        }
        viewModel.repeatPasswordValidityStringCode.observe(viewLifecycleOwner){ repeatPasswordValidityStringCode ->
            if (repeatPasswordValidityStringCode != null) binding.repeatPasswordInput.error = getString(repeatPasswordValidityStringCode)
        }
        viewModel.passwordsMatchStringCode.observe(viewLifecycleOwner){passwordsMatch ->
            if (passwordsMatch != null) binding.repeatPasswordInput.error = getString(passwordsMatch)
        }
        viewModel.registerButtonIsEnabled.observe(viewLifecycleOwner){registerButtonIsEnabled ->
            binding.registerButton.isEnabled = registerButtonIsEnabled
        }
        viewModel.registerResponse.observe(viewLifecycleOwner){ registerResponse->
            if(viewModel.validateEmail(registerResponse)) {
                dialog.dismiss()
                val directions = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(true)
                findNavController().navigate(directions)
            }else{
                dialog.dismiss()
                val bottomSheetBinding: RegisterLoginResultBottomSheetBinding = RegisterLoginResultBottomSheetBinding.inflate(layoutInflater)
                with(bottomSheetBinding){
                    callbackIcon.setImageResource(R.drawable.fail)
                    callbackText.text = getString(R.string.registration_failed)
                    callbackDescription.text = registerResponse
                }
                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
            }
        }
        initializeUI()
    }

    private fun initializeUI(){
        viewModel.initRegisterTextInputListeners(binding.emailInput , binding.passwordInput , binding.repeatPasswordInput)
        setUpRegisterButton()
    }

    private fun setUpRegisterButton(){
        binding.registerButton.setOnClickListener{
            viewModel.onRegisterButtonClicked(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )
            val loadingBottomSheetBinding: LoadingBottomSheetBinding = LoadingBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(loadingBottomSheetBinding.root)
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}