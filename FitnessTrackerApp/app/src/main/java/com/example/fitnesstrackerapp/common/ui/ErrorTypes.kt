package com.example.fitnesstrackerapp.common.ui

sealed class RegistrationError {
    object None : RegistrationError()
    object NameLengthError : RegistrationError()
    object ProhibitedWordsError : RegistrationError()
}