package com.example.fitnesstrackerapp

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.fitnesstrackerapp.dashboard.ui.screens.DashboardScreen
import com.example.fitnesstrackerapp.login.ui.screens.LoginScreen
import com.example.fitnesstrackerapp.login.ui.screens.RegistrationScreen
import com.example.fitnesstrackerapp.login.ui.screens.SplashScreen
import com.example.fitnesstrackerapp.ui.theme.FitnessTrackerAppTheme

class FitnessTrackerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Set system bar color
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FitnessTrackerAppTheme {
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
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavigationItem.Splash.route) {
            SplashScreen(navigateToLoginScreen = { navController.navigate(route = NavigationItem.Login.route) }) { displayName ->
                navController.navigate("${NavigationItem.Dashboard.route}/$displayName")
            }
        }
        composable(route = NavigationItem.Login.route) {
            LoginScreen(navigateToRegistration = { navController.navigate(route = NavigationItem.Registration.route) })
        }
        composable(route = NavigationItem.Registration.route) {
            RegistrationScreen { displayName ->
                navController.navigate("${NavigationItem.Dashboard.route}/$displayName")
            }
        }
        composable(
            route = "${NavigationItem.Dashboard.route}/{displayName}",
            arguments = listOf(navArgument("displayName") { type = NavType.StringType })
        ) { backstackEntry ->
            DashboardScreen(displayName = backstackEntry.arguments?.getString("displayName") ?: "")
        }
    }
}