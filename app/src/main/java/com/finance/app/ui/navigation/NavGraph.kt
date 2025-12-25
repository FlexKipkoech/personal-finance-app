package com.finance.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.finance.app.ui.screens.AddBudgetScreen
import com.finance.app.ui.screens.AddTransactionScreen
import com.finance.app.ui.screens.BudgetsScreen
import com.finance.app.ui.screens.HomeScreen
import com.finance.app.ui.screens.ReportsScreen
import com.finance.app.ui.screens.SettingsScreen
import com.finance.app.viewmodel.FinanceViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddTransaction = { navController.navigate(Screen.AddTransaction.route) }
            )
        }
        
        composable(Screen.Reports.route) {
            ReportsScreen(viewModel = viewModel)
        }
        
        composable(Screen.Budgets.route) {
            BudgetsScreen(
                viewModel = viewModel,
                onAddBudget = { navController.navigate(Screen.AddBudget.route) }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel = viewModel)
        }
        
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AddBudget.route) {
            AddBudgetScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
