package com.example.fitnesstrackerapp

enum class Screen {
    DASHBOARD,
    LOGIN,
    REGISTRATION,
    SPLASH
}

sealed class NavigationItem(val route: String) {
    object Splash : NavigationItem(Screen.SPLASH.name)
    object Login : NavigationItem(Screen.LOGIN.name)
    object Registration : NavigationItem(Screen.REGISTRATION.name)
    object Dashboard : NavigationItem(Screen.DASHBOARD.name)
}