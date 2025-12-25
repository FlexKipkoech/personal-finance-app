package com.finance.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Filled.Home, "Home"),
    BottomNavItem(Screen.Reports, Icons.Filled.Assessment, "Reports"),
    BottomNavItem(Screen.Budgets, Icons.Filled.AccountBalance, "Budgets"),
    BottomNavItem(Screen.Settings, Icons.Filled.Settings, "Settings")
)
