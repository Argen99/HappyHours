package com.example.presentation.ui.fragments.sign_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.core.Constants
import com.example.core_ui.base.BaseFragment
import com.example.core_ui.extensions.dateFormatter
import com.example.core_ui.extensions.gone
import com.example.core_ui.extensions.setupDateTextWatcher
import com.example.core_ui.extensions.showShortToast
import com.example.core_ui.extensions.visible
import com.example.domain.models.UserRegisterRequest
import com.example.presentation.databinding.FragmentSignUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {
    override fun getViewBinding() = FragmentSignUpBinding.inflate(layoutInflater)
    override val viewModel by viewModel<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etUserPassword.addTextChangedListener {
            validatePass(it.toString())
        }

        binding.etEnterBirth.setupDateTextWatcher()

        binding.btnNext.setOnClickListener {
            checkInputData()
        }
    }

    private fun containsDigits(text: String): Boolean {
        return text.any { it.isDigit() }
    }

    private fun checkInputData(): Unit = with(binding) {
        if (etEnterName.text.toString().isEmpty()) {
            showShortToast("Enter your name")
        }else if (containsDigits(etEnterName.text.toString())){
            showShortToast("Name should not be empty and contains digits")
        } else if (etEnterBirth.text.toString().length < 10) {
            showShortToast("Enter your birthday")
        } else if (etEnterEmail.text.toString().isEmpty()) {
            showShortToast("Enter your email")
        } else if (etUserPassword.text.toString().isEmpty()) {
            showShortToast("Enter password")
        } else if (etUserPassword.text.toString() != etRePass.text.toString()) {
            showShortToast("Passwords do not match")
        } else {
            viewModel.userRegister(
                UserRegisterRequest(
                    name = etEnterName.text.toString(),
                    email = etEnterEmail.text.toString(),
                    datOfBirth = etEnterBirth.text.toString().dateFormatter(),
                    password = etUserPassword.text.toString(),
                    passwordConfirm = etRePass.text.toString()
                )
            )
        }
    }

    override fun launchObservers() {
        viewModel.registerState.spectateUiState(
            success = {
                navigateToMain()
            },
            error = {
                showShortToast(it)
            }
        )
    }

    private fun navigateToMain() {
        val request = NavDeepLinkRequest.Builder
            .fromUri(Constants.Deeplink.DEEPLINK_NAV_TO_MAIN_MODULE.toUri())
            .build()
        findNavController().navigate(request)
    }

    @SuppressLint("ResourceAsColor")
    fun validatePass(password: String) {
        if (password.isEmpty()) {
            binding.tvPassValidLength.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.example.core_ui.R.color.correct_false_text
                )
            )
        } else {
            binding.tvPassValidLength.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.example.core_ui.R.color.correct_true_text
                )
            )
        }
        if (password.length < 8) {
            binding.tvPassValidUpLowCase.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.example.core_ui.R.color.correct_false_text
                )
            )
        } else {
            binding.tvPassValidUpLowCase.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.example.core_ui.R.color.correct_true_text
                )
            )
        }
    }

    override fun onBackPressed() {
        findNavController().popBackStack()
    }
}