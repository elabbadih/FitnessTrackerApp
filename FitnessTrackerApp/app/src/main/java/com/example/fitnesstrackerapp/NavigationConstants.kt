package com.example.fitnesstrackerapp

enum class Screen {
    HOME,
    LOGIN,
    REGISTRATION,
    SPLASH
}

sealed class NavigationItem(val route: String) {
    object Splash : NavigationItem(Screen.SPLASH.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Registration : NavigationItem(Screen.REGISTRATION.name)
    object Home : NavigationItem(Screen.HOME.name)
}