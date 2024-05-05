package com.education.flashwiseapp.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.flashwiseapp.common.ui.FirebaseAuthResponse
import com.education.flashwiseapp.common.ui.RegistrationError
import com.education.flashwiseapp.common.util.EMAIL_REGEX
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _splashLoginState = MutableStateFlow<String?>(null)
    val splashLoginState: StateFlow<String?> = _splashLoginState

    private val _registrationUserState = MutableStateFlow(FirebaseAuthResponse.NONE)
    val registrationUserState: StateFlow<FirebaseAuthResponse> = _registrationUserState

    private val _signInUserState = MutableStateFlow(FirebaseAuthResponse.NONE)
    val signInUserState: StateFlow<FirebaseAuthResponse> = _signInUserState

    /**
     * Updates the value of loginState with the currentUser logged in status
     */
    fun isUserLoggedIn() {
        // Get user logged in status
        viewModelScope.launch {
            _splashLoginState.value = auth.currentUser?.displayName ?: ""
        }
    }

    /**
     * @param email Email value to sign in the user
     * @param password Password value to sign in the user
     *
     * Takes the email and password and attempts sign in through Firebase Auth
     * Updates StateFlow with the status of the auth task
     */
    fun onUserSubmitLogin(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _signInUserState.value = FirebaseAuthResponse.SUCCESS
                    } else {
                        _signInUserState.value = FirebaseAuthResponse.FAILURE
                    }
                }
        }
    }

    fun resetFirebaseUserStates() {
        _registrationUserState.value = FirebaseAuthResponse.NONE
        _signInUserState.value = FirebaseAuthResponse.NONE
    }

    fun resetUserPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email sent
                } else {
                    // Handle error
                }
            }
    }

    /**
     * @param email Email value to register the new account
     * @param password Password value to register the new account
     *
     * Takes the email and password and creates a new user through Firebase Auth
     * Updates StateFlow with the status of the auth task
     */
    fun onRegisterNewUser(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registrationUserState.value = FirebaseAuthResponse.SUCCESS
                    } else {
                        _registrationUserState.value = FirebaseAuthResponse.FAILURE
                    }
                }
        }
    }

    /**
     * Validation functions
     */
    fun validateRegistrationInput(email: String): RegistrationError {

        // Validate length
        if (email.length !in 2..50) {
            return RegistrationError.LengthError
        }

        // Validate email
        val regex = EMAIL_REGEX.toRegex()
        if (!regex.matches(email)) {
            return RegistrationError.RegexError
        }

        return RegistrationError.None
    }
}