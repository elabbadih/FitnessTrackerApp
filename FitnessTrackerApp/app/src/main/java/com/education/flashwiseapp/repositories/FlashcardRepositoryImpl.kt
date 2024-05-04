package com.education.flashwiseapp.repositories

import com.education.flashwiseapp.dashboard.model.Flashcard
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

interface FlashcardRepository {
    fun getFlashcards(uid: String): Flow<Result<List<Flashcard>>>
    fun addFlashcard(flashcard: Flashcard, uid: String): Flow<Result<Flashcard>>
    fun getSubjects(uid: String): Flow<Result<List<String>>>
    fun addSubject(subject: String, uid: String): Flow<Result<String>>
}

@Singleton
class FlashcardRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) :
    FlashcardRepository {

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

        val reference = getFlashcardsReference(uid)
        reference.addListenerForSingleValueEvent(postListener)
        awaitClose {
            reference.removeEventListener(postListener)
        }
    }

    override fun addFlashcard(
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

        val reference = getFlashcardsReference(uid).push()
        reference.setValue(flashcard)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this@callbackFlow.trySendBlocking(Result.success(flashcard))
                } else {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            task.exception ?: UnknownError(
                                "Unknown error"
                            )
                        )
                    )
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

    override fun getSubjects(uid: String) = callbackFlow<Result<List<String>>> {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subjects = mutableListOf<String>()
                for (postSnapshot in snapshot.children) {
                    val subject = postSnapshot.getValue(String::class.java)
                    subject?.let {
                        subjects.add(it)
                    }
                }
                this@callbackFlow.trySendBlocking(Result.success(subjects))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        val reference = getSubjectsReference(uid)
        reference.addListenerForSingleValueEvent(postListener)
        awaitClose {
            reference.removeEventListener(postListener)
        }
    }

    override fun addSubject(subject: String, uid: String) = callbackFlow<Result<String>> {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Not needed for adding Subjects
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }

        val reference = getSubjectsReference(uid).push()
        reference.setValue(subject)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    this@callbackFlow.trySendBlocking(Result.success(subject))
                } else {
                    this@callbackFlow.trySendBlocking(
                        Result.failure(
                            task.exception ?: UnknownError(
                                "Unknown error"
                            )
                        )
                    )
                }
            }
            .addOnFailureListener { e ->
                this@callbackFlow.trySendBlocking(Result.failure(e))
            }

        reference.addListenerForSingleValueEvent(postListener)
        awaitClose {
            reference.removeEventListener(postListener)
        }
    }

    private fun getFlashcardsReference(uid: String): DatabaseReference {
        return firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("flashcards")
    }

    private fun getSubjectsReference(uid: String): DatabaseReference {
        return firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("subjects")
    }
}