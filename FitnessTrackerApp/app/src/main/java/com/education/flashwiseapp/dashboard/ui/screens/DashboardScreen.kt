package com.education.flashwiseapp.dashboard.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.education.flashwiseapp.common.ui.FirebaseAuthResponse
import com.education.flashwiseapp.dashboard.model.Flashcard
import com.education.flashwiseapp.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    val signOutStatus by viewModel.userSignOut.collectAsState(FirebaseAuthResponse.NONE)
    val subjects by viewModel.subjects.collectAsState(listOf())
    val flashcards by viewModel.flashcards.collectAsState(listOf())

    LaunchedEffect(Unit) {
        viewModel.getSubjects()
        viewModel.getFlashcards()
    }

    when (signOutStatus) {
        FirebaseAuthResponse.SUCCESS -> {
            navigateToLogin()
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        subjects.forEach { subject ->
            var isExpanded by remember { mutableStateOf(false) }
            val subjectFlashcards = flashcards.filter { it.subject == subject }

            ExpandableSubjectItem(
                subject = subject,
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded })

            if (isExpanded) {
                flashcards.forEach { flashcard ->
                    Text(text = flashcard.question)
                }
            }
        }
    }
}

@Composable
fun ExpandableSubjectItem(
    subject: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isExpanded) Color.Black else Color.Gray
    val arrowIcon =
        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val clickModifier = Modifier.clickable(onClick = onClick)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .then(clickModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = subject, modifier = Modifier.weight(1f))
        Icon(
            imageVector = arrowIcon,
            contentDescription = "Expand/Collapse Arrow",
            tint = borderColor
        )
    }
}