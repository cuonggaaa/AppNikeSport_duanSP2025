package com.example.smeb9716.activities

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.ActRegisterBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.utils.validator.validateAddress
import com.example.smeb9716.utils.validator.validateConfirmPassword
import com.example.smeb9716.utils.validator.validateName
import com.example.smeb9716.utils.validator.validatePassword
import com.example.smeb9716.utils.validator.validatePhone
import com.example.smeb9716.utils.validator.validateUsername
import com.example.smeb9716.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterAct : BaseActivity<ActRegisterBinding>() {
    companion object {
        private const val TAG = "RegisterAct"
    }

    private val viewModel: RegisterViewModel by viewModels()

    override fun getViewBinding(): ActRegisterBinding {
        return ActRegisterBinding.inflate(layoutInflater)
    }

    override fun initViews() {

    }

    override fun initEvents() {
        binding.tvSignIn.setOnClickListener {
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            if (validateInput()) {
                val request = RegisterRequest(
                    name = binding.edtName.text.toString(),
                    phone = binding.edtPhone.text.toString(),
                    email = binding.edtEmail.text.toString(),
                    password = binding.edtPassword.text.toString(),
                    address = binding.edtAddress.text.toString()
                )
                viewModel.registerUser(request)
            }
        }

        binding.edtName.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateName()
            binding.tilName.error = errMsg
        }

        binding.edtPhone.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validatePhone()
            binding.tilPhone.error = errMsg
        }

        binding.edtEmail.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateUsername()
            binding.tilEmail.error = errMsg
        }

        binding.edtPassword.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validatePassword()
            binding.tilPassword.error = errMsg
        }

        binding.edtConfirmPassword.doOnTextChanged { text, _, _, _ ->
            val errMsg =
                text.toString().validateConfirmPassword(binding.edtPassword.text.toString())
            binding.tilConfirmPassword.error = errMsg
        }

        binding.edtAddress.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateAddress()
            binding.tilAddress.error = errMsg
        }
    }

    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                showLoading(it)
            }
        }
        lifecycleScope.launch {
            viewModel.responseMessage.collect {
                showMessage(this@RegisterAct, it.message, it.bgType)
            }
        }
        viewModel.registerSuccess.observe(this) {
            if (it) {
                backToLogin(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            }
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()
        val name = binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val address = binding.edtAddress.text.toString()

        val emailErr = email.validateUsername()
        val passwordErr = password.validatePassword()
        val confirmPasswordErr = confirmPassword.validateConfirmPassword(password)
        val nameErr = name.validateName()
        val phoneErr = phone.validatePhone()
        val addressErr = address.validateAddress()

        binding.tilEmail.error = emailErr
        binding.tilPassword.error = passwordErr
        binding.tilConfirmPassword.error = confirmPasswordErr
        binding.tilName.error = nameErr
        binding.tilPhone.error = phoneErr
        binding.tilAddress.error = addressErr

        return emailErr == null && passwordErr == null && confirmPasswordErr == null && nameErr == null && phoneErr == null && addressErr == null
    }

    private fun backToLogin(email: String, password: String) {
        val intent = Intent().apply {
            putExtra(LoginAct.EMAIL_KEY, email)
            putExtra(LoginAct.PASSWORD_KEY, password)
        }
        setResult(LoginAct.RESULT_CODE, intent)
        finish()
    }
}