package com.andikas.assetdash.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andikas.assetdash.ui.screens.add_transaction.AddTransactionScreen
import com.andikas.assetdash.ui.screens.dashboard.DashboardScreen
import com.andikas.assetdash.ui.screens.detail.DetailScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onCoinClick = { coinId ->
                    navController.navigate("${Screen.Detail.route}/$coinId")
                },
                onAddTransactionClick = {
                    navController.navigate(Screen.AddTransaction.route)
                }
            )
        }

        composable(
            route = "${Screen.Detail.route}/{coinId}",
            arguments = listOf(
                navArgument("coinId") {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreen(navController = navController)
        }

        composable(route = Screen.AddTransaction.route) {
            AddTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}