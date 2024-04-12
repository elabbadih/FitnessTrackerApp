package com.example.fitnesstrackerapp.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstrackerapp.common.ui.RegistrationError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _splashLoginState = MutableStateFlow<String?>(null)
    val splashLoginState: StateFlow<String?> = _splashLoginState

    private val _registrationUserState = MutableStateFlow<String>("")
    val registrationUserState: StateFlow<String> = _registrationUserState

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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG_X", "Login successful")
                    val user = auth.currentUser
                    // Let composable know login successful
                } else {
                    Log.d("TAG_X", "Login failure")
                    // Toast.makeText()
                    // Let composable know login failure
                }
            }
    }

    fun validateNameInput(fullName: String, prohibitedWords: List<String>): RegistrationError {

        // Validate length
        if (fullName.length !in 2..50) {
            return RegistrationError.NameLengthError
        }

        // Validate prohibited words
        // TODO Fix prohibited words validation
//        val foundProhibitedWord = prohibitedWords.find { fullName.contains(it, ignoreCase = true) }
//        if (foundProhibitedWord != null) {
//            return RegistrationError.ProhibitedWordsError
//        }

        // Validate email
        // TODO Email validation

        return RegistrationError.None
    }

    /**
     * @param email Email value to register the new account
     * @param password Password value to register the new account
     *
     * Takes the email and password and creates a new user through Firebase Auth
     * Updates StateFlow with the status of the auth task
     */
    fun onRegisterNewUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG_X", "Registration successful")
                    storeNewUserName(name, auth.currentUser)
                } else {
                    Log.d("TAG_X", "Registration failure")
                    // Toast.makeText()
                    // Let composable know registration failure
                }
            }
    }

    private fun storeNewUserName(name: String, currentUser: FirebaseUser?) {
        val profileUpdate = userProfileChangeRequest {
            displayName = name
        }
        currentUser?.also { user ->
            user.updateProfile(profileUpdate)
                .addOnCompleteListener { task ->
                    user.displayName?.let { name ->
                        if (task.isSuccessful) {
                            _registrationUserState.value = name
                        } else {
                            // Update user displayName failure state
                        }
                    }
                }
        }
    }
}