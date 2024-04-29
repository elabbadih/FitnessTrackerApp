package com.example.fitnesstrackerapp.repositories

import com.example.fitnesstrackerapp.dashboard.model.Flashcard
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

interface FlashcardRepository {
    fun addCardToDatabase(flashcard: Flashcard, uid: String): Flow<Result<Flashcard>>
    fun getFlashcards(uid: String): Flow<Result<List<Flashcard>>>
}

@Singleton
class FlashcardRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) :
    FlashcardRepository {

    override fun addCardToDatabase(
        flashcard: Flashcard,
        uid: String
    ) = callbackFlow<Result<Flashcard>> {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Not needed for adding Flashcard
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        val reference = firebaseDatabase.reference
            .child("users")
            .child(uid)
            .push()

        reference.setValue(flashcard)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this@callbackFlow.trySendBlocking(Result.success(flashcard))
                } else {
                    this@callbackFlow.trySendBlocking(Result.failure(task.exception ?: UnknownError("Unknown error")))
                }
            }
            .addOnFailureListener { e ->
                this@callbackFlow.trySendBlocking(Result.failure(e))
            }

        reference.addValueEventListener(postListener)

        awaitClose {
            reference.removeEventListener(postListener)
        }
    }

    override fun getFlashcards(uid: String) = callbackFlow<Result<List<Flashcard>>> {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val flashcardList = mutableListOf<Flashcard>()
                for (postSnapshot in snapshot.children) {
                    val flashcard = postSnapshot.getValue(Flashcard::class.java)
                    flashcard?.let {
                        flashcardList.add(it)
                    }
                }
                this@callbackFlow.trySendBlocking(Result.success(flashcardList))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        val reference = firebaseDatabase.reference
            .child("users")
            .child(uid)

        reference.addListenerForSingleValueEvent(postListener)

        awaitClose {
            reference.removeEventListener(postListener)
        }
    }
}