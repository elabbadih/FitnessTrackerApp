package com.example.fitnesstrackerapp.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstrackerapp.NavigationItem
import com.example.fitnesstrackerapp.R

@Composable
fun FlashWiseScaffold(
    onNavigationItemSelected: (NavigationItem) -> Unit,
    content: @Composable () -> Unit
) {
    var selectedItem by remember { mutableStateOf(NavigationItem.Dashboard as NavigationItem) }

    Scaffold(
        topBar = {
            CustomTopAppBar()
        },
        bottomBar = {
            CustomBottomAppBar(
                selectedItem = selectedItem,
                onItemSelected = { navItem ->
                    selectedItem = navItem
                    onNavigationItemSelected(navItem)
                }
            )
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
            containerColor = colors.tertiary,
            titleContentColor = colors.onPrimary,
            actionIconContentColor = colors.primary,
            navigationIconContentColor = colors.primary,
            scrolledContainerColor = colors.primary
        ),
        title = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = "Flashcard App",
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
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(80.dp),
        containerColor = colorScheme.primary,
        contentColor = colorScheme.secondary
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            BottomNavItemIcon(
                isSelected = selectedItem == NavigationItem.Dashboard,
                icon = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = stringResource(id = R.string.bottom_app_bar_item_1),
                onClick = {
                    if (selectedItem != NavigationItem.Dashboard) {
                        onItemSelected(NavigationItem.Dashboard)
                    }
                }
            )
            BottomNavItemIcon(
                isSelected = selectedItem == NavigationItem.Create,
                icon = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.bottom_app_bar_item_2),
                onClick = {
                    if (selectedItem != NavigationItem.Create) {
                        onItemSelected(NavigationItem.Create)
                    }
                }
            )
            BottomNavItemIcon(
                isSelected = selectedItem == NavigationItem.Alarm,
                icon = Icons.Default.Alarm,
                contentDescription = stringResource(id = R.string.bottom_app_bar_item_3),
                onClick = {
                    if (selectedItem != NavigationItem.Alarm) {
                        onItemSelected(NavigationItem.Alarm)
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavItemIcon(
    isSelected: Boolean,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color.Black else Color.Gray
    Box(modifier = Modifier.size(42.dp)) {
        IconButton(onClick = { onClick() }) {
            Icon(imageVector = icon, contentDescription = contentDescription, tint = color)
        }
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
            CustomBottomAppBar(NavigationItem.Dashboard, {})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {}
    }
}