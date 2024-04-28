package com.example.flashfocusapp

enum class Screen {
    ALARM,
    CREATE,
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
    object Create : NavigationItem(Screen.CREATE.name)
    object Alarm : NavigationItem(Screen.ALARM.name)
}