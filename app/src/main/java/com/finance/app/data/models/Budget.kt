package com.finance.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val amount: Double,
    val period: BudgetPeriod,
    val spent: Double = 0.0,
    val startDate: Long,
    val endDate: Long,
    val alertThreshold: Double = 0.8,
    val firebaseId: String? = null
)

enum class BudgetPeriod {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
