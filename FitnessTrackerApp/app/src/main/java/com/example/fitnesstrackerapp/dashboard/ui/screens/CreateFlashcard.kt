package com.example.fitnesstrackerapp.dashboard.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitnesstrackerapp.dashboard.viewmodel.DashboardViewModel

@Composable
fun CreateFlashcard(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val maxQuestionLength = 100
    val maxAnswerLength = 200

    // Subjects for dropdown list
    val createCardSubjects by viewModel.cardSubjects.collectAsState(listOf(""))

    // Create button enabled status
    val createButtonEnabled by viewModel.createButtonEnabled.collectAsState(true)

    val addCardResult by viewModel.addCardResult.collectAsState("")

    if (addCardResult.isNotEmpty()) {
        Toast.makeText(context, addCardResult, Toast.LENGTH_SHORT).show()
    }

    // State for selected subject
    var selectedSubject by remember {
        mutableStateOf<String?>(null)
    }
    var subjectExpanded by remember {
        mutableStateOf(false)
    }

    // State for difficulty level
    var difficultyLevel by remember {
        mutableIntStateOf(1)
    }

    // States for questions and answers
    var question by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Flashcards"
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Subject:")
                // Subject dropdown list
                TextButton(
                    onClick = { subjectExpanded = true }, modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        ), // Add background and corner radius
                    contentPadding = PaddingValues(16.dp), // Add padding to the button content
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Black) // Set text color
                ) {
                    Text(text = getSubjectSelected(selectedSubject))
                }
            }

            DropdownMenu(
                expanded = subjectExpanded,
                onDismissRequest = { subjectExpanded = false },
            ) {
                createCardSubjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(text = subject) },
                        onClick = {
                            selectedSubject = subject
                            subjectExpanded = false
                        }
                    )
                }
                DropdownMenuItem(text = { Text(text = "Add New Subject") }, onClick = { /*TODO*/ })
            }
        }

        Text(text = "Difficulty level:")
        Slider(
            value = difficultyLevel.toFloat(),
            onValueChange = { difficultyLevel = it.toInt() },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.padding(horizontal = 48.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 1..5) {
                Text(text = "$i")
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        // Question and Answer inputs fields
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = question,
                onValueChange = {
                    if (it.length <= maxQuestionLength) {
                        question = it
                    }
                },
                label = { Text(text = "Question") },
                minLines = 1,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "${question.length}/$maxQuestionLength",
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    if (it.length <= maxAnswerLength) {
                        answer = it
                    }
                },
                label = { Text(text = "Answer") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${answer.length}/$maxAnswerLength",
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        // Button to create flashcard
        Button(
            enabled = createButtonEnabled,
            onClick = {
                val formValid = checkFormValid(selectedSubject, question, answer)
                if (formValid.first) {
                    viewModel.addFlashcard(question, answer, difficultyLevel)
                } else {
                    Toast.makeText(context, formValid.second, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Create Flashcard")
        }
    }
}

private fun getSubjectSelected(selectedSubject: String?): String {
    return if (selectedSubject.isNullOrEmpty()) {
        "Select Subject"
    } else {
        selectedSubject
    }
}

private fun checkFormValid(
    selectedSubject: String?,
    question: String,
    answer: String
): Pair<Boolean, String> {
    return when {
        selectedSubject.isNullOrEmpty() -> {
            Pair(false, "Select subject")
        }
        question.isEmpty() -> {
            Pair(false, "Enter question")
        }
        answer.isEmpty() -> {
            Pair(false, "Enter answer")
        }
        else -> {
            Pair(true, "")
        }
    }
}