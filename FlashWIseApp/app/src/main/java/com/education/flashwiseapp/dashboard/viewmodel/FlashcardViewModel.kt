package com.education.flashwiseapp.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.flashwiseapp.dashboard.model.Flashcard
import com.education.flashwiseapp.repositories.FlashcardRepositoryImpl
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val repository: FlashcardRepositoryImpl,
    private val user: FirebaseUser?
) : ViewModel() {

    /**
     * Create Flashcards
     */
    private val _flashcards = MutableStateFlow(listOf<Flashcard>())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards

    private val _cardSubjects = MutableStateFlow(listOf<String>())
    val cardSubjects: StateFlow<List<String>> = _cardSubjects

    private val _addCardResult = MutableStateFlow("")
    val addCardResult: StateFlow<String> = _addCardResult

    private val _addSubjectResult = MutableStateFlow("")
    val addSubjectResult: StateFlow<String> = _addSubjectResult

    private val _createButtonEnabled = MutableStateFlow(true)
    val createButtonEnabled: StateFlow<Boolean> = _createButtonEnabled

    fun createFlashcard(subject: String, question: String, answer: String, difficulty: Int) {
        val flashcard = Flashcard(
            subject = subject,
            question = question,
            answer = answer,
            difficulty = difficulty,
            dateCreated = System.currentTimeMillis()
        )
        viewModelScope.launch {
            user?.let { user ->
                _createButtonEnabled.value = false
                repository.addFlashcard(flashcard = flashcard, uid = user.uid)
                    .collect { result ->
                        when {
                            result.isSuccess -> {
                                _addCardResult.value = "Flashcard added successfully"
                            }

                            else -> {
                                _addCardResult.value = "Error adding flashcard"
                            }
                        }
                        _createButtonEnabled.value = true
                    }
            }
        }
    }

    fun createSubject(subject: String) {
        viewModelScope.launch {
            user?.let {
                repository.addSubject(subject = subject, uid = user.uid)
                    .collect { result ->
                        result.onSuccess { subject ->
                            // Let user know subject added successfully, update subject list
                            _addSubjectResult.value = subject
                        }
                        result.onFailure { e ->
                            // Handle failure here
                        }
                    }
            }
        }
    }

    fun getSubjects() {
        viewModelScope.launch {
            user?.let {
                repository.getSubjects(user.uid)
                    .collect { result ->
                        result.onSuccess { subjectsList ->
                            // Update list of subjects
                            _cardSubjects.value = subjectsList
                        }
                        result.onFailure { e ->
                            // Handle failure here
                        }
                    }
            }
        }
    }

    fun resetAddCardResult() {
        _addCardResult.value = ""
    }

    fun resetAddSubjectResult() {
        _addSubjectResult.value = ""
    }
}