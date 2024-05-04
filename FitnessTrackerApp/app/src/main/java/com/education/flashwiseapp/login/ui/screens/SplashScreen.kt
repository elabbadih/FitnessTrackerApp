package com.education.flashwiseapp.login.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.education.flashwiseapp.login.viewmodel.LoginViewModel
import com.education.flashwiseapp.ui.theme.LightModePrimary

@Composable
fun SplashScreen(
    viewModel: LoginViewModel = viewModel(),
    navigateToLogin: () -> Unit,
    navigateToDashboard: () -> Unit
) {
    
    LaunchedEffect(Unit) {
        viewModel.isUserLoggedIn()
    }

    // Observe user logged in status on launch
    val splashLoginState by viewModel.splashLoginState.collectAsState(null)

    // Use the displayName to determine if user is logged in
    // If displayName is not empty, navigate to Dashboard, otherwise Login screen
    splashLoginState?.also { displayName ->
        if (displayName.isNotEmpty()) {
            navigateToDashboard()
        } else {
            navigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightModePrimary)
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading...",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}