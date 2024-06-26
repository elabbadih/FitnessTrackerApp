package com.education.flashwiseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.education.flashwiseapp.common.ui.FlashWiseScaffold
import com.education.flashwiseapp.dashboard.ui.screens.AlarmScreen
import com.education.flashwiseapp.dashboard.ui.screens.CreateFlashcard
import com.education.flashwiseapp.dashboard.ui.screens.DashboardScreen
import com.education.flashwiseapp.login.ui.screens.LoginScreen
import com.education.flashwiseapp.login.ui.screens.RegistrationScreen
import com.education.flashwiseapp.login.ui.screens.SplashScreen
import com.education.flashwiseapp.ui.theme.FlashWiseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashWiseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Set system bar color
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FlashWiseAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {
                    AppNavHost(navController = rememberNavController())
                }
            }
        }
    }

    private fun sharedPrefExample() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "sharedPref",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Splash.route
) {
    FlashWiseScaffold(
        onNavigationItemSelected = { item ->
            when (item) {
                NavigationItem.Dashboard -> {
                    navController.navigate(route = NavigationItem.Dashboard.route)
                }

                NavigationItem.Create -> {
                    navController.navigate(route = NavigationItem.Create.route)
                }

                NavigationItem.Alarm -> {
                    navController.navigate(route = NavigationItem.Alarm.route)
                }

                else -> {}
            }
        }
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(route = NavigationItem.Splash.route) {
                SplashScreen(
                    navigateToLogin = { navController.navigate(route = NavigationItem.Login.route) },
                    navigateToDashboard = { navController.navigate(route = NavigationItem.Dashboard.route) }
                )
            }
            composable(route = NavigationItem.Login.route) {
                LoginScreen(
                    navigateToRegistration = { navController.navigate(route = NavigationItem.Registration.route) },
                    navigateToDashboard = { navController.navigate(route = NavigationItem.Dashboard.route) }
                )
            }
            composable(route = NavigationItem.Registration.route) {
                RegistrationScreen(navigateToDashboard = { navController.navigate(route = NavigationItem.Dashboard.route) })
            }
            composable(route = NavigationItem.Dashboard.route) {
                DashboardScreen(navigateToLogin = { navController.navigate(route = NavigationItem.Login.route) })
            }
            composable(route = NavigationItem.Create.route) {
                CreateFlashcard()
            }
            composable(route = NavigationItem.Alarm.route) {
                AlarmScreen()
            }
        }
    }

}