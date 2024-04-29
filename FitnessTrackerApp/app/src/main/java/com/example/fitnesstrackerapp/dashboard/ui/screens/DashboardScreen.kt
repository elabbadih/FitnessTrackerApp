package com.example.fitnesstrackerapp.dashboard.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstrackerapp.common.ui.FirebaseAuthResponse
import com.example.fitnesstrackerapp.dashboard.model.Flashcard
import com.example.fitnesstrackerapp.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {

    val displayName by viewModel.displayUserNameState.collectAsState("")
    val signOutStatus by viewModel.userSignOut.collectAsState(FirebaseAuthResponse.NONE)

    val flashcards by viewModel.flashcards.collectAsState(listOf())

    when (signOutStatus) {
        FirebaseAuthResponse.SUCCESS -> {
            navigateToLogin()
        }

        else -> {}
    }

    viewModel.getFlashcards()

    Log.d("TAG_X", "Flashcards: $flashcards")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        flashcards.forEach {
            Text(text = "Flashcard: ${it.question}")
        }
    }
}