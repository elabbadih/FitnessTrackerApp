package com.education.flashwiseapp.common.ui

sealed class RegistrationError {
    object None : RegistrationError()
    object NameLengthError : RegistrationError()
}

enum class FirebaseAuthResponse {
    NONE,
    SUCCESS,
    FAILURE
}