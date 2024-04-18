package com.example.fitnesstrackerapp

enum class Screen {
    DASHBOARD,
    EXERCISES,
    LOGIN,
    NOTES,
    REGISTRATION,
    SETTINGS,
    SPLASH
}

sealed class NavigationItem(val route: String) {
    object Splash : NavigationItem(Screen.SPLASH.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Registration : NavigationItem(Screen.REGISTRATION.name)
    object Dashboard : NavigationItem(Screen.DASHBOARD.name)
    object Exercises : NavigationItem(Screen.EXERCISES.name)
    object Notes : NavigationItem(Screen.NOTES.name)
    object Settings : NavigationItem(Screen.SETTINGS.name)
}