package com.education.flashwiseapp.login.ui.screens

import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.education.flashwiseapp.R
import com.education.flashwiseapp.common.ui.RegistrationError
import com.education.flashwiseapp.common.ui.FirebaseAuthResponse
import com.education.flashwiseapp.common.util.PASSWORD_LENGTH
import com.education.flashwiseapp.login.viewmodel.LoginViewModel

@Composable
fun RegistrationScreen(
    viewModel: LoginViewModel = viewModel(),
    navigateToDashboard: () -> Unit
) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passConfirm by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val registrationUserState by viewModel.registrationUserState.collectAsState("")

    when (registrationUserState) {
        FirebaseAuthResponse.SUCCESS -> {
            navigateToDashboard()
        }

        FirebaseAuthResponse.FAILURE -> {
            makeText(context, "Something went wrong!", LENGTH_SHORT).show()
            viewModel.resetFirebaseUserStates()
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { emailInput -> email = emailInput },
            label = { Text(text = stringResource(id = R.string.email_address_label)) },
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { passInput ->
                if (passInput.length <= PASSWORD_LENGTH) password = passInput
            },
            label = { Text(text = stringResource(id = R.string.password_label)) },
            textStyle = MaterialTheme.typography.bodyMedium,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) stringResource(id = R.string.password_description_hide) else stringResource(
                            id = R.string.password_description_show
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passConfirm,
            onValueChange = { passInput ->
                if (passInput.length <= PASSWORD_LENGTH) passConfirm = passInput
            },
            label = { Text(text = stringResource(id = R.string.password_confirm)) },
            textStyle = MaterialTheme.typography.bodyMedium,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (isPasswordVisible) stringResource(id = R.string.password_description_hide) else stringResource(
                            id = R.string.password_description_show
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Reset error message upon submission
                errorMessage = ""

                if (password == passConfirm) {
                    isLoading = true
                    when (viewModel.validateRegistrationInput(email = email.trim())) {
                        RegistrationError.LengthError -> {
                            errorMessage = "Email input incorrect."
                        }

                        else -> {
                            viewModel.onRegisterNewUser(email, password)
                        }
                    }
                } else {
                    errorMessage = "Passwords do not match."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(text = stringResource(id = R.string.create_account_button))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}