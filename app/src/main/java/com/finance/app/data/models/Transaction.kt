package com.finance.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val type: TransactionType,
    val description: String,
    val date: Long = Date().time,
    val isRecurring: Boolean = false,
    val recurringFrequency: String? = null,
    val synced: Boolean = false,
    val firebaseId: String? = null
)

enum class TransactionType {
    INCOME, EXPENSE
}
