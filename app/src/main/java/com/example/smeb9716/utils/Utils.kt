package com.example.smeb9716.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.smeb9716.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object Utils {
    private const val TAG = "Utils"

    // Show a short toast system
    fun showToastSystem(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Show a custom toast
    fun showCustomToast(
        activity: Activity,
        context: Context,
        title: String,
        message: String,
        motionToastStyle: MotionToastStyle
    ) {
        try {
            MotionToast.darkToast(
                activity,
                title,
                message,
                motionToastStyle,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, R.font.montserrat_bold)
            )
        } catch (e: Exception) {
            showToastSystem(context, message)
        }
    }

    // Validate Postal Code
    private fun validatePostalCode(value: String): Boolean {
        val postalCode = value.trim()
        return if (postalCode.length < 5 || postalCode.length > 6) {
            Log.d(TAG, "validatePostalCode: Invalid postal code")
            false
        } else {
            true
        }
    }

    // Validate Empty Fields
    private fun validateNotEmpty(value: String): Boolean {
        val input = value.trim()
        return if (input.isEmpty()) {
            Log.d(TAG, "validateNotEmpty: Empty input")
            false
        } else {
            true
        }
    }

    // Validate Email
    private fun validateEmail(value: String): Boolean {
        val email = value.trim()
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.d(TAG, "validateEmail: Invalid email")
            false
        } else {
            true
        }
    }

    // Validate Password
    private fun validatePassword(value: String): Boolean {
        val password = value.trim()
        if (password.length < 8) {
            Log.d(TAG, "validatePassword: Password must be at least 8 characters long")
            return false
        }
        if (!password.matches(Regex(".*[A-Z].*"))) {
            Log.d(TAG, "validatePassword: Password must contain at least one uppercase letter")
            return false
        }
        if (!password.matches(Regex(".*[a-z].*"))) {
            Log.d(TAG, "validatePassword: Password must contain at least one lowercase letter")
            return false
        }
        if (!password.matches(Regex(".*\\d.*"))) {
            Log.d(TAG, "validatePassword: Password must contain at least one digit")
            return false
        }
        if (!password.matches(Regex(".*[!@#$%^&*+=?-].*"))) {
            Log.d(TAG, "validatePassword: Password must contain at least one special character")
            return false
        }
        return true
    }

    // Validate Confirm Password
    private fun validateConfirmPassword(value1: String, value2: String): Boolean {
        val password = value1.trim()
        val confirmPassword = value2.trim()
        return if (password != confirmPassword) {
            Log.d(TAG, "validateConfirmPassword: Password and confirm password do not match")
            false
        } else {
            true
        }
    }

    // Validate Phone Number
    private fun validatePhoneNumber(value: String): Boolean {
        val phoneNumber = value.trim()
        return if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            Log.d(TAG, "validatePhoneNumber: Invalid phone number")
            false
        } else {
            true
        }
    }
}
