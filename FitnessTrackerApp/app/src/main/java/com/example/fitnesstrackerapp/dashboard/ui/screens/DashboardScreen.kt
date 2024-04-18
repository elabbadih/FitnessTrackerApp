package com.example.fitnesstrackerapp.dashboard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstrackerapp.common.ui.FirebaseAuthResponse
import com.example.fitnesstrackerapp.dashboard.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    navigateToLogin: () -> Unit
) {

    val displayName by viewModel.displayUserNameState.collectAsState("")
    val signOutStatus by viewModel.userSignOut.collectAsState(FirebaseAuthResponse.NONE)

    when (signOutStatus) {
        FirebaseAuthResponse.SUCCESS -> {
            navigateToLogin()
        }

        else -> {}
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome $displayName!"
        )
    }
}