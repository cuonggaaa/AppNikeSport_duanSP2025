package com.example.smeb9716.activities

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.smeb9716.MainActivity
import com.example.smeb9716.databinding.ActSplashBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.model.PREF_EMAIL
import com.example.smeb9716.model.PREF_PASSWORD
import com.example.smeb9716.utils.PreferManager
import com.example.smeb9716.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashAct : BaseActivity<ActSplashBinding>() {
    companion object {
        private const val SPLASH_DELAY = 1000L
    }

    private lateinit var preferManager: PreferManager

    val viewModel: LoginViewModel by viewModels()

    override fun getViewBinding(): ActSplashBinding {
        return ActSplashBinding.inflate(layoutInflater)
    }

    override fun initViews() {
    }

    override fun initEvents() {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                canLogin()
            }, SPLASH_DELAY)
        } catch (e: Exception) {
            canLogin()
        }
    }

    override fun initObservers() {
        // No observers to initialize for now
        viewModel.loginResponse.observe(this) {
            if (it != null) {
                openMainScreen()
            }
        }
        viewModel.isSplashLoginFail.observe(this) {
            if (it) {
                openLoginScreen()
            }
        }
    }

    private fun canLogin() {
        preferManager = PreferManager.getInstance(applicationContext)
        val rememberEmail = preferManager.readString(PREF_EMAIL, null)
        val rememberPassword = preferManager.readString(PREF_PASSWORD, null)


        if (rememberEmail != null && rememberPassword != null) {
            viewModel.login(rememberEmail, rememberPassword)
        } else {
            openLoginScreen()
        }
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginScreen() {
        val intent = Intent(this, LoginAct::class.java)
        startActivity(intent)
        finish()
    }
}
