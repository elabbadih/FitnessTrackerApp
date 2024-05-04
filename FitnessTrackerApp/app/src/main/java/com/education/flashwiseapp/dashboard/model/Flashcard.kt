package com.education.flashwiseapp.dashboard.model

data class Flashcard(
    val question: String = "",
    val answer: String = "",
    val difficulty: Int = 0,
    val dateCreated: Long = 0
)