package com.example.fitnesstrackerapp.homepage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackerapp.common.ui.FitnessTrackerLayout

@Composable
fun HomepageScreen(
    modifier: Modifier = Modifier,
    username: String
) {
    FitnessTrackerLayout(content = {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome $username!",
                modifier = modifier
            )
        }
    })
}