package com.finance.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Reports : Screen("reports")
    object Budgets : Screen("budgets")
    object Settings : Screen("settings")
    object AddTransaction : Screen("add_transaction")
    object AddBudget : Screen("add_budget")
}
