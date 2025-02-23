package com.example.smeb9716.utils.validator

fun String.validateName(): String? {
    return if (this.isEmpty()) {
        "Name is required"
    } else {
        null
    }
}

fun String.validateNotEmpty(): String? {
    return if (this.isEmpty()) {
        "This field is required"
    } else {
        null
    }
}

fun String.validatePhone(): String? {
    var errorMessage: String? = null
    if (this.isEmpty()) {
        errorMessage = "Phone number is required"
    } else if (this.length != 10) {
        errorMessage = "Phone number must be 10 digits"
    } else if (!this.matches(Regex("^[0-9]*\$"))) {
        errorMessage = "Phone number must contain only digits"
    }
    return errorMessage
}

fun String.validateConfirmPassword(password: String): String? {
    return if (this.isEmpty()) {
        "Confirm password is required"
    } else if (this != password) {
        "Password and confirm password do not match"
    } else {
        null
    }
}

fun String.validateAddress(): String? {
    return if (this.isEmpty()) {
        "Address is required"
    } else {
        null
    }
}