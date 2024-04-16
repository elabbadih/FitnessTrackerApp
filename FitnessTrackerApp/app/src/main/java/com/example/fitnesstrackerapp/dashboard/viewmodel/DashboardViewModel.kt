package com.example.fitnesstrackerapp.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitnesstrackerapp.common.ui.FirebaseAuthResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel: ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _displayUserNameState = MutableStateFlow("")
    val displayUserNameState: StateFlow<String> = _displayUserNameState

    private val _userSignOut = MutableStateFlow(FirebaseAuthResponse.NONE)
    val userSignOut: StateFlow<FirebaseAuthResponse> = _userSignOut

    init {
        updateDisplayUserName()
    }

    fun signOut() {
        auth.signOut()
        _userSignOut.value = FirebaseAuthResponse.SUCCESS
    }

    private fun updateDisplayUserName() {
        _displayUserNameState.value = auth.currentUser?.displayName ?: ""
    }
}