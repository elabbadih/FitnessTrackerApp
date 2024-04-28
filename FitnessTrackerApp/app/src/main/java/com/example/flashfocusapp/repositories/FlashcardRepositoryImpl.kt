package com.example.flashfocusapp.repositories

import com.example.flashfocusapp.dashboard.model.Flashcard
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface FlashcardRepository {
    suspend fun addCardToDatabase(flashcard: Flashcard, uid: String): Flow<Result<Unit>>
    fun getFlashcards(uid: String)
}

@Singleton
class FlashcardRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) :
    FlashcardRepository {

    override suspend fun addCardToDatabase(flashcard: Flashcard, uid: String): Flow<Result<Unit>> =
        flow {
            val reference = firebaseDatabase.reference
                .child("users")
                .child(uid)
                .push()

            try {
                var result: Result<Unit>? = null
                reference.setValue(flashcard)
                    .addOnSuccessListener {
                        result = Result.success(Unit)
                    }
                    .addOnFailureListener {
                        result = Result.failure(it)
                    }
                result?.let { emit(it) }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    override fun getFlashcards(uid: String) {
        val reference = firebaseDatabase.reference
            .child("users")
            .child(uid)
            .push()
    }
}