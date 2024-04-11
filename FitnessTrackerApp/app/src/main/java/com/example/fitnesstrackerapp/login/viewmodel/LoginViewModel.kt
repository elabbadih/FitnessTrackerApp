package com.example.fitnesstrackerapp.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _loginState = MutableStateFlow<Boolean?>(null)
    val loginState: StateFlow<Boolean?> = _loginState

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() {
        // Get user logged in status
        viewModelScope.launch {
            _loginState.value = (auth.currentUser != null)
        }
    }

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

    fun onRegisterNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG_X", "Registration successful")
                    val user = auth.currentUser
                    // Let composable know registration successful
                } else {
                    Log.d("TAG_X", "Registration failure")
                    // Toast.makeText()
                    // Let composable know registration failure
                }
            }
    }
}