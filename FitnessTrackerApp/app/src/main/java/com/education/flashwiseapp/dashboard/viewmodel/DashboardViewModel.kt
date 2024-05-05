package com.education.flashwiseapp.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.flashwiseapp.common.ui.FirebaseAuthResponse
import com.education.flashwiseapp.dashboard.model.Flashcard
import com.education.flashwiseapp.repositories.FlashcardRepositoryImpl
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FlashcardRepositoryImpl,
    private val user: FirebaseUser?
) : ViewModel() {

    /**
     * Dashboard
     */
    private val _userSignOut = MutableStateFlow(FirebaseAuthResponse.NONE)
    val userSignOut: StateFlow<FirebaseAuthResponse> = _userSignOut

    private val _subjects = MutableStateFlow(listOf<String>())
    val subjects: StateFlow<List<String>> = _subjects

    private val _flashcards = MutableStateFlow(listOf<Flashcard>())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards

    init {
        viewModelScope.launch {

        }
    }

    fun signOut() {
        Firebase.auth.signOut()
        _userSignOut.value = FirebaseAuthResponse.SUCCESS
    }

    fun getSubjects() {
        viewModelScope.launch {
            user?.let {
                repository.getSubjects(uid = user.uid)
                    .collect { result ->
                        result.onSuccess {  subjects ->
                            _subjects.value = subjects
                        }
                        result.onFailure { e ->
                            // Handle failure here
                        }
                    }
            }
        }
    }

    fun getFlashcards() {
        viewModelScope.launch {
            user?.let {
                repository.getFlashcards(uid = user.uid)
                    .collect { result ->
                        result.onSuccess { flashcards ->
                            _flashcards.value = flashcards
                        }
                        result.onFailure { e ->
                            // Handle failure here
                        }
                    }
            }
        }
    }
}