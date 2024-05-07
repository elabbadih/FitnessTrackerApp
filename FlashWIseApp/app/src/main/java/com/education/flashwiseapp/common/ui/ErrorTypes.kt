package com.education.flashwiseapp.common.ui

sealed class RegistrationError {
    object None : RegistrationError()
    object LengthError : RegistrationError()
    object RegexError: RegistrationError()
}

enum class FirebaseAuthResponse {
    NONE,
    SUCCESS,
    FAILURE
}