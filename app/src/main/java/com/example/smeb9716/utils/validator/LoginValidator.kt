package com.example.smeb9716.utils.validator

fun String.validateEmail(): String? {
    var errorMessage: String? = null
    if (this.isEmpty()) {
        errorMessage = "Email is required"
    } else if (!this.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
        errorMessage = "Invalid email"
    }
    return errorMessage
}

fun String.validateUsername(): String? {
    var errorMessage: String? = null
    if (this.isEmpty()) {
        errorMessage = "Email is required"
    }
    return errorMessage
}

//fun String.validatePassword(): String? {
//    return when {
//        this.isEmpty() -> "Password is required"
//        this.length < 8 -> "Password must be at least 8 characters"
//        !this.matches(Regex(".*[A-Z].*")) -> "Password must contain at least one uppercase letter"
//        !this.matches(Regex(".*[a-z].*")) -> "Password must contain at least one lowercase letter"
//        !this.matches(Regex(".*\\d.*")) -> "Password must contain at least one digit"
//        !this.matches(Regex(".*[!@#\$%^&*+=?-].*")) -> "Password must contain at least one special character"
//        else -> null
//    }
//}

fun String.validatePassword(): String? {
    return when {
        this.isEmpty() -> "Password is required"
        else -> null
    }
}