package com.example.fitnesstrackerapp.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstrackerapp.R

// TODO Set up onClick actions for BottomAppBar
@Composable
fun FitnessTrackerLayout(
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar()
        },
        bottomBar = {
            CustomBottomAppBar({})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() {
    val colors = colorScheme

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = colors.primary,
            titleContentColor = colors.onPrimary,
            actionIconContentColor = colors.primary,
            navigationIconContentColor = colors.primary,
            scrolledContainerColor = colors.primary
        ),
        title = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = "Fitness Tracker",
                    style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun CustomBottomAppBar(
    onItemSelected: () -> Unit
) {
    BottomAppBar(
        containerColor = colorScheme.primaryContainer,
        contentColor = colorScheme.primary
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            BottomBarButton(
                text = stringResource(R.string.bottom_app_bar_item_1),
                isSelected = true,
                onClick = onItemSelected
            )
            BottomBarButton(
                text = stringResource(R.string.bottom_app_bar_item_2),
                isSelected = true,
                onClick = onItemSelected
            )
            BottomBarButton(
                text = stringResource(R.string.bottom_app_bar_item_3),
                isSelected = true,
                onClick = onItemSelected
            )
            BottomBarButton(
                text = stringResource(R.string.bottom_app_bar_item_4),
                isSelected = true,
                onClick = onItemSelected
            )
        }
    }
}

// TODO Replace Text with icons
@Composable
fun BottomBarButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) colorScheme.primary else colorScheme.surface,
            contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, color = Color.Black)
    ) {
        Text(text = text, style = TextStyle(fontSize = 9.sp))
    }
}

@Preview
@Composable
fun PreviewScaffold() {
    Scaffold(
        topBar = {
            CustomTopAppBar()
        },
        bottomBar = {
            CustomBottomAppBar({})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}