package com.example.smeb9716.activities

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.MainActivity
import com.example.smeb9716.databinding.ActLoginBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.model.PREF_EMAIL
import com.example.smeb9716.model.PREF_PASSWORD
import com.example.smeb9716.utils.PreferManager
import com.example.smeb9716.utils.validator.validatePassword
import com.example.smeb9716.utils.validator.validateUsername
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginAct : BaseActivity<ActLoginBinding>() {
    companion object {
        private const val TAG = "LoginAct"
        const val RESULT_CODE = 200
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
    }

    private lateinit var preferManager: PreferManager
    private lateinit var registerLauncher: ActivityResultLauncher<Intent>


    override fun getViewBinding(): ActLoginBinding {
        return ActLoginBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        preferManager = PreferManager.getInstance(applicationContext)
        binding.checkboxRememberLogin.isChecked = false

        registerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_CODE && result.data != null) {
                    val data: Intent? = result.data
                    val email = data?.getStringExtra(EMAIL_KEY)
                    val password = data?.getStringExtra(PASSWORD_KEY)
                    binding.edtEmail.setText(email)
                    binding.edtPassword.setText(password)
                }
            }
    }

    override fun initEvents() {
        binding.btnLogin.setOnClickListener { handleLogin() }
        binding.tvSignUp.setOnClickListener { handleRegister() }

        binding.edtEmail.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateUsername()

            if (errMsg != null) {
                binding.tilEmail.error = errMsg
            } else {
                binding.tilEmail.error = null
            }
        }
        binding.edtPassword.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                binding.tilPassword.error = "Password is required"
            } else {
                binding.tilPassword.error = null
            }
        }
    }

    override fun initObservers() {

    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleLogin() {
//       navigateToMain()

        // Validate
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        val emailError = email.validateUsername()
        val passwordError = password.validatePassword()

        if (emailError != null || passwordError != null) {
            binding.tilEmail.error = emailError
            binding.tilPassword.error = passwordError
            return
        }

    }

    private fun handleRegister() {
        val intent = Intent(this, RegisterAct::class.java)
        registerLauncher.launch(intent)
    }
}
