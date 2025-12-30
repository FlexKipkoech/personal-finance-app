package com.finance.app.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("sign_up")

    object Home : Screen("home")
    object Reports : Screen("reports")
    object Budgets : Screen("budgets")
    object Settings : Screen("settings")
    object AddTransaction : Screen("add_transaction")
    object AddBudget : Screen("add_budget")
}
