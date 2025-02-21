package com.example.hofa.navigation

sealed class Screen(
    val route: String
) {
    object Home: Screen(
        route = ROUTE_HOME
    )

    object Settings: Screen(
        route = ROUTE_SETTINGS
    )

    object Statistics: Screen(
        route = ROUTE_STATISTICS
    )

    object Seance: Screen(
        route = ROUTE_SEANCE
    )





    private companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_STATISTICS = "statistics"
        const val ROUTE_SEANCE = "seance"

    }
}