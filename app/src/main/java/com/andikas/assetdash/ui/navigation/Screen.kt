package com.andikas.assetdash.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard_screen")
    object Detail : Screen("detail_screen")
}